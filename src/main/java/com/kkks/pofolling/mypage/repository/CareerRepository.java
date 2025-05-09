package com.kkks.pofolling.mypage.repository;

import com.kkks.pofolling.mypage.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;
import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {
    List<Career> findByUserUserId(Long userId);
}
