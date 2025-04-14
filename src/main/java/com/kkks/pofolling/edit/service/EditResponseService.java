package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.edit.dto.RequestEditDetailResponseDTO;
import com.kkks.pofolling.edit.dto.RequestEditsResponseDTO;
import com.kkks.pofolling.edit.entity.EditRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EditResponseService {

    Page<RequestEditsResponseDTO> getRequestEditList(Pageable pageable);

    RequestEditDetailResponseDTO getRequestEditDetail(Long editRequestId);

    void startEdit(Long mentorId, Long editRequestId);
}
