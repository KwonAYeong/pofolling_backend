package com.kkks.pofolling.mypage.repository;

import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // User정보와 포트폴리오상태로 조회된 포트폴리오들 가져오기.
    List<Portfolio> findByUserAndStatus(User user, PortfolioStatus status);


}
