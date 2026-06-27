package org.devsync.spring.attachment.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String store(MultipartFile file);

    Resource load(String storedFilename);

    void delete(String storedFilename);

}
