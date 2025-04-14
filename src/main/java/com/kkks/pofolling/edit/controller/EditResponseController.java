package com.kkks.pofolling.edit.controller;

import com.kkks.pofolling.edit.dto.RequestEditDetailResponseDTO;
import com.kkks.pofolling.edit.dto.RequestEditsResponseDTO;
import com.kkks.pofolling.edit.service.EditResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/edit-response")
public class EditResponseController {

    private final EditResponseService editResponseService;

    @Autowired
    public EditResponseController(EditResponseService editResponseService) {
        this.editResponseService = editResponseService;
    }

    // 요청된 첨삭 리스트 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Page<RequestEditsResponseDTO>>> getRequestEditList(
            @PageableDefault(size = 20, sort = "requestedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<RequestEditsResponseDTO> result = editResponseService.getRequestEditList(pageable);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 요청된 첨삭 세부정보 조회
    @GetMapping("/{editRequestId}")
    public ResponseEntity<ApiResponse<RequestEditDetailResponseDTO>> getRequestEditDetail(
            @PathVariable Long editRequestId) {
        RequestEditDetailResponseDTO result = editResponseService.getRequestEditDetail(editRequestId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 첨삭 시작
    @PatchMapping("/{editRequestId}/{mentorId}")
    public ResponseEntity<ApiResponse<Void>> startEdit(
            @PathVariable Long editRequestId,
            @PathVariable Long mentorId
    ) {
        editResponseService.startEdit(mentorId, editRequestId);
        return ResponseEntity.ok(ApiResponse.successWithMessage(200, "첨삭이 시작되었습니다."));
    }

}
