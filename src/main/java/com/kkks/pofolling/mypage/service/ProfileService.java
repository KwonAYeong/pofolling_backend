package com.kkks.pofolling.mypage.service;

import com.kkks.pofolling.mypage.dto.ProfileResponseDTO;
import com.kkks.pofolling.mypage.dto.ProfileUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    ProfileResponseDTO getProfile(Long userId);
    void updateProfile(Long userId, ProfileUpdateDTO updateDTO);
    boolean isNicknameAvailable(Long userId, String nickname);
    String updateProfileImage(Long userId, MultipartFile file) throws IOException;
}
