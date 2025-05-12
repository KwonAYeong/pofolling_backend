package com.kkks.pofolling.user.repository;

import com.kkks.pofolling.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname (String nickname);

}
