package com.kkks.pofolling.edit.repository;

import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EditRequestRepository extends JpaRepository<EditRequest,Long> {
    Page<EditRequest> findByPortfolio_Status(PortfolioStatus status, Pageable pageable);

    // 최신 첨삭요청한 포트폴리오 추출
    Optional<EditRequest> findTopByPortfolioOrderByRequestedAtDesc(Portfolio portfolio);


}

