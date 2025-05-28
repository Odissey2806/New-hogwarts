package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

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
        logger.info("Был вызван метод для загрузки аватара для студента с id = {}", studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("Студент с id = {} не найден", studentId);
                    return new ResourceNotFoundException("Студент с id " + studentId + " не найден");
                });

        Path filePath = Paths.get(avatarsDir, studentId + "." + getExtension(avatarFile.getOriginalFilename()));
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
        logger.debug("Аватар для студента с id = {} успешно сохранен", studentId);
    }


    private String getExtension(String fileName) {
        logger.debug("Получение расширения файла для {}", fileName);
        if (fileName == null || fileName.lastIndexOf(".") + 1 == fileName.length()) {
            logger.warn("Имя файла null или не содержит расширения: {}", fileName);
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(Long avatarId) {
        logger.info("Был вызван метод для поиска аватара по id = {}", avatarId);
        return avatarRepository.findById(avatarId)
                .orElseThrow(() -> {
                    logger.error("Аватар с id = {} не найден", avatarId);
                    return new ResourceNotFoundException("Аватар с id " + avatarId + " не найден");
                });
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Был вызван метод для получения всех аватаров, страница = {}, размер = {}", page, size);
        if (page < 0 || size <= 0) {
            logger.warn("Некорректные параметры пагинации: страница={}, размер={}", page, size);
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        return avatarRepository.findAll(pageRequest);
    }
}
