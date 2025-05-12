package com.kkks.pofolling.mypage.repository;

import com.kkks.pofolling.mypage.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByUserUserId(Long userId);
}
