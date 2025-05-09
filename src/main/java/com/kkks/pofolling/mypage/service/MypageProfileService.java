package com.kkks.pofolling.mypage.service;

import com.kkks.pofolling.mypage.dto.MypageProfileResponseDTO;
import com.kkks.pofolling.mypage.dto.MypageProfileUpdateDTO;

public interface MypageProfileService {
    MypageProfileResponseDTO getProfile(Long userId);
    void updateProfile(Long userId, MypageProfileUpdateDTO updateDTO);
}
