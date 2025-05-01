package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.ResourceNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final String avatarsDir;


    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         @Value("${avatars.dir.path}") String avatarsDir) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.avatarsDir = avatarsDir;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Студент с id " + studentId + " не найден"));

        Path filePath = Paths.get(avatarsDir, studentId + "." + getExtension(avatarFile.getOriginalFilename()));;
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }


        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        avatarRepository.save(avatar);
    }


    private String getExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") + 1 == fileName.length()) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    public Avatar findAvatar(Long avatarId) {
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> new ResourceNotFoundException("Аватар с id " + avatarId + " не найден"));
    }

}
