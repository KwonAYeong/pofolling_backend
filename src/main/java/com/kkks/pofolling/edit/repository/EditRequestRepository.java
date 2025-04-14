package com.kkks.pofolling.edit.repository;

import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditRequestRepository extends JpaRepository<EditRequest,Long> {
    Page<EditRequest> findByPortfolio_Status(PortfolioStatus status, Pageable pageable);
}

