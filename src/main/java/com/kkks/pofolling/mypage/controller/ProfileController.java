package com.kkks.pofolling.mypage.controller;

import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.mypage.dto.ProfileResponseDTO;
import com.kkks.pofolling.mypage.dto.ProfileUpdateDTO;
import com.kkks.pofolling.mypage.service.ProfileService;
import com.kkks.pofolling.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage/profile")
public class ProfileController {

    private final ProfileService mypageProfileService;
    private final UserRepository userRepository;

    // 마이페이지 - 프로필 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable Long userId) {
        ProfileResponseDTO profile = mypageProfileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    // 마이페이지 - 프로필 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateProfile(@PathVariable Long userId,
                                              @RequestBody ProfileUpdateDTO updateDTO) {
        mypageProfileService.updateProfile(userId, updateDTO);
        return ResponseEntity.ok("회원정보가 수정되었습니다.");
    }

    // 닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean isAvailable = !userRepository.existsByNickname(nickname);
        if (!isAvailable) {
            throw new BusinessException(ExceptionCode.NICKNAME_ALREADY_EXISTS);
        }
        return ResponseEntity.ok(Map.of("isAvailable", true));
    }


}
