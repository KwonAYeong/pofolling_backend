package com.kkks.pofolling.edit.controller;

import com.kkks.pofolling.edit.dto.EditRequestDTO;
import com.kkks.pofolling.edit.dto.RegisteredPortfolioResponseDTO;
import com.kkks.pofolling.edit.service.EditRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/edit-requests")
public class EditRequestController {
    private final EditRequestService editRequestService;

    @Autowired
    public EditRequestController(EditRequestService editRequestService) {
        this.editRequestService = editRequestService;
    }

    @GetMapping("/{userId}") // 유저의 등록된 포트폴리오를 가져오는 컨트롤러
    public ResponseEntity<ApiResponse<List<RegisteredPortfolioResponseDTO>>> getEditRequestList(@PathVariable Long userId) {
        List<RegisteredPortfolioResponseDTO> result = editRequestService.getRegisteredPf(userId);

        if (result.isEmpty() ) {
            return ResponseEntity.ok(ApiResponse.successWithMessage(200, "등록된 포트폴리오가 없습니다."));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> requestEdit(@RequestBody EditRequestDTO dto) {
        editRequestService.requestEdit(dto.getPortfolioId(), dto.getMenteeId());
        return ResponseEntity.ok(ApiResponse.successWithMessage(204, "등록에 성공했습니다."));
    }

}
