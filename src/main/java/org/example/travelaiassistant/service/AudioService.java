package org.example.travelaiassistant.service;

import org.example.travelaiassistant.dto.AudioUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class AudioService {

    @Value("${app.audio.upload-dir}")
    private String uploadDir;

    public AudioUploadResponse store(MultipartFile file) {
        try {

            Path audioDir = Path.of(uploadDir);
            Files.createDirectories(audioDir);

            String fileId = UUID.randomUUID().toString();
            String storedFilename = fileId + "_" + file.getOriginalFilename();
            Path targetPath = audioDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath);

            return AudioUploadResponse.builder()
                    .fileId(fileId)
                    .originalFilename(file.getOriginalFilename())
                    .storedFilename(storedFilename)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .build();

        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }

    }
}
