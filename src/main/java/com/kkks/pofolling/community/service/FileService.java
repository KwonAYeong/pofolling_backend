package com.kkks.pofolling.community.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file);
    void deleteFile(String storedFileName);
}
