package com.kkks.pofolling.mypage.controller;

import com.kkks.pofolling.mypage.dto.PortfolioCreateDTO;
import com.kkks.pofolling.mypage.dto.PortfolioDetailResponseDTO;
import com.kkks.pofolling.mypage.dto.PortfolioListResponseDTO;
import com.kkks.pofolling.mypage.dto.PortfolioUpdateDTO;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.mypage.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    // 포트폴리오 등록
    @PostMapping
    public ResponseEntity<String> createPortfolio(@RequestParam Long userId,
                                                @RequestBody @Valid PortfolioCreateDTO createDTO) {
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

    // 포트폴리오 상세 조회
    @GetMapping("/{portfolioId}")
    public ResponseEntity<PortfolioDetailResponseDTO> getPortfolioDetail(@PathVariable Long portfolioId) {
        PortfolioDetailResponseDTO portfolio = portfolioService.getPortfolioDetail(portfolioId);

        return ResponseEntity.ok(portfolio);
    }

    // 포트폴리오 수정
    @PatchMapping("/{portfolioId}")
    public ResponseEntity<String> updatePortfolio(@PathVariable Long portfolioId,
                                                  @RequestBody PortfolioUpdateDTO updateDTO) {
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
