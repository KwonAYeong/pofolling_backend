package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.edit.dto.RegisteredPortfolioResponseDTO;

import java.util.List;


public interface EditRequestService {

    //mentee의 등록된 포트폴리오들 가져오기.
    List<RegisteredPortfolioResponseDTO> getRegisteredPf(Long userId);

    //이력 첨삭 상태값 변경 및 EditRequest 엔티티 생성.
    void requestEdit(Long portfolioId, Long menteeId);

}
