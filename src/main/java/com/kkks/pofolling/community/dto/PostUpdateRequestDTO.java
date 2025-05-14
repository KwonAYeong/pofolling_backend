package com.kkks.pofolling.community.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class PostUpdateRequestDTO {
    private String title;
    private String content;

    // 삭제할 파일의 S3 URL 목록
    private List<String> deleteFileUrls;

    // 업데이트할 파일의 포지션 (예: fileUrl1) → 파일명 (예: image1.jpg)
    private Map<String, String> updatedFilePositions;
}
