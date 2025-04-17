package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.edit.dto.EditDetailResponseDTO;
import com.kkks.pofolling.edit.dto.EditListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EditResponseService {

    Page<EditListResponseDTO> getRequestEditList(Pageable pageable);

    EditDetailResponseDTO getRequestEditDetail(Long editRequestId);

    void startEdit(Long mentorId, Long editRequestId);
}
