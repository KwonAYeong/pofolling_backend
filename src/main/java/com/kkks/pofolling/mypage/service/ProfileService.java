package com.kkks.pofolling.mypage.service;

import com.kkks.pofolling.mypage.dto.ProfileResponseDTO;
import com.kkks.pofolling.mypage.dto.ProfileUpdateDTO;

public interface ProfileService {
    ProfileResponseDTO getProfile(Long userId);
    void updateProfile(Long userId, ProfileUpdateDTO updateDTO);
}
