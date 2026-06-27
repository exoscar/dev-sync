package org.devsync.spring.attachment.service;

import lombok.RequiredArgsConstructor;
import org.devsync.spring.common.config.StorageProperties;
import org.devsync.spring.common.exception.BusinessException;
import org.devsync.spring.common.exception.ErrorCode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    private final Path uploadPath;

    public LocalFileStorageService(StorageProperties storageProperties) {
        this.uploadPath = Paths.get(storageProperties.uploadDir());
    }


    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("Invalid file", ErrorCode.BAD_REQUEST);
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String storedFilename = UUID.randomUUID() + extension;
        try {
            Files.createDirectories(uploadPath);
            Path destination = uploadPath.resolve(storedFilename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            return storedFilename;
        } catch (IOException ex) {
            throw new BusinessException("Unable to store file", ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Resource load(String storedFilename) {
        Path filePath = uploadPath.resolve(storedFilename).normalize();
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(!resource.exists()||
            !resource.isReadable()){
                throw new BusinessException(
                        "File not found",
                        ErrorCode.NOT_FOUND
                );
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new BusinessException(
                    "File not found",
                    ErrorCode.NOT_FOUND
            );

        }
    }

    @Override
    public void delete(String storedFilename) {
        Path filePath =
                uploadPath.resolve(storedFilename).normalize();
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new BusinessException(
                    "Unable to delete file",
                    ErrorCode.INTERNAL_SERVER_ERROR
            );
        }

    }
}
