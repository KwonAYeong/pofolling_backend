package com.kkks.pofolling.community.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class PostUpdateRequestDTO {
    private String title;
    private String content;

    // 삭제할 파일의 위치를 나타냄: 예) "fileUrl2", "fileUrl3"
    private List<String> deleteFilePosition;

    // 수정할 파일의 위치를 나타냄: 예) fileUrl1: exampleFile.pdf
    private Map<String, MultipartFile> updatedFiles;
}
