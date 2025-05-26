package com.kkks.pofolling.community.controller;

import com.kkks.pofolling.community.dto.FileUploadResponseDTO;
import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.s3.S3Uploader;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatFileController {

    private final S3Uploader s3Uploader;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponseDTO> uploadChatFile(@RequestPart("file") MultipartFile file) {
        try {
            String url = s3Uploader.upload(file, "chat");
            return ResponseEntity.ok(new FileUploadResponseDTO(url));
        } catch (IOException e) {
            throw new BusinessException(ExceptionCode.FILE_UPLOAD_FAILED);
        }
    }


}
