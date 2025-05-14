package com.kkks.pofolling.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
public class S3Uploader {
    private final S3Client s3Client;
    private final String bucket = "pofolling-bucket";

    @Autowired
    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    // 파일 업로드 로직
    public String upload(MultipartFile file, String dirName) throws IOException {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

        return "https://" + bucket + ".s3.amazonaws.com/" + fileName;
    }

    // 파일 삭제 로직
    public void delete(String fileUrl) {
        String fileKey = extractFileKey(fileUrl);

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private String extractFileKey(String fileUrl) {
        // https://bucket-name.s3.amazonaws.com/community/uuid_filename.jpg
        return fileUrl.substring(fileUrl.indexOf(".com/") + 5);
    }

}
