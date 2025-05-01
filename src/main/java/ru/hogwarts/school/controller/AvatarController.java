package ru.hogwarts.school.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId,
                                               @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() == 0) {
            return ResponseEntity.badRequest().body("Файл не должен быть пустым.");
        }
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().body("Аватар для студента с id " + studentId + " успешно загружен.");
    }

    @GetMapping(value = "/{avatarId}/from-db")
    public ResponseEntity<byte[]> downloadAvatarFromDb(@PathVariable Long avatarId) {
        Avatar avatar = avatarService.findAvatar(avatarId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getFileSize());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{avatarId}/from-file")
    public ResponseEntity<Resource> downloadAvatarFromFile(@PathVariable Long avatarId) throws IOException {
        Avatar avatar = avatarService.findAvatar(avatarId);
        Path path = Paths.get(avatar.getFilePath());

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            InputStreamResource resource = new InputStreamResource(inputStream);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                    .contentLength(avatar.getFileSize())
                    .body(resource);
        }
    }
}