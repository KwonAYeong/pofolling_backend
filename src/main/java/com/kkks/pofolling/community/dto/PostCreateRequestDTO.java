package com.kkks.pofolling.community.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class PostCreateRequestDTO {
    private String title;
    private String content;
    private List<MultipartFile> files;
}
