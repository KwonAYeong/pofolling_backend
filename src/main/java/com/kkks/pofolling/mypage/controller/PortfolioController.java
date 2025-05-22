package com.kkks.pofolling.mypage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkks.pofolling.mypage.dto.*;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.mypage.service.PortfolioService;
import com.kkks.pofolling.s3.S3Uploader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final S3Uploader s3Uploader;

    // 포트폴리오 등록
    @PostMapping
    public ResponseEntity<String> createPortfolio(@RequestParam("userId") Long userId,
                                                @RequestPart("file") MultipartFile file,
                                                @RequestPart("data") String dataJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        PortfolioCreateDTO createDTO = objectMapper.readValue(dataJson, PortfolioCreateDTO.class);

        String fileUrl = s3Uploader.upload(file, "portfolio");
        createDTO.setFileUrl(fileUrl);

        Long portfolioId = portfolioService.createPortfolio(userId, createDTO);

        return ResponseEntity.ok("포트폴리오 등록 완료");
    }

    // 포트폴리오 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<PortfolioListResponseDTO>> getMyPortfolios(@RequestParam Long userId,
                                                                          @RequestParam(required = false)PortfolioStatus status) {
        List<PortfolioListResponseDTO> portfolios = portfolioService.getMyPortfolios(userId, status);

        return ResponseEntity.ok(portfolios);
    }

    // 포트폴리오 상세 조회 (멘티용)
    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioMenteeDetailResponseDTO> getMenteePortfolioDetail(@PathVariable Long portfolioId) {
        PortfolioMenteeDetailResponseDTO portfolio = portfolioService.getMenteePortfolioDetail(portfolioId);

        return ResponseEntity.ok(portfolio);
    }

    // 포트폴리오 수정
    @PatchMapping("/{portfolioId}")
    public ResponseEntity<String> updatePortfolio(
            @PathVariable Long portfolioId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("data") String dataJson) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        PortfolioUpdateDTO updateDTO = objectMapper.readValue(dataJson, PortfolioUpdateDTO.class);

        // 파일이 새로 들어오면 기존 파일 삭제 후 새 파일 업로드
        if (file != null) {
            String oldUrl = portfolioService.getFileUrl(portfolioId);
            s3Uploader.delete(oldUrl);

            String newUrl = s3Uploader.upload(file, "portfolio");
            updateDTO.setFileUrl(newUrl);
        }

        portfolioService.updatePortfolio(portfolioId, updateDTO);
        return ResponseEntity.ok("포트폴리오 수정 완료");
    }

    // 포트폴리오 삭제
    @DeleteMapping("/{portfolioId}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long portfolioId) {
        portfolioService.deletePortfolio(portfolioId);

        return ResponseEntity.ok("포트폴리오 삭제 완료");
    }

}
