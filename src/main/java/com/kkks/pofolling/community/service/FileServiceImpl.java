package com.kkks.pofolling.community.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadFile(MultipartFile file) {
        // 실제 저장은 생략하고 파일명만 가공
        String uuid = UUID.randomUUID().toString();
        String storedFileName = uuid + "_" + file.getOriginalFilename();

        // 예: S3 URL 대신 임시 파일 경로처럼 리턴
        return "/files/" + storedFileName;
    }

    @Override
    public void deleteFile(String storedFileName) {

    }
}
