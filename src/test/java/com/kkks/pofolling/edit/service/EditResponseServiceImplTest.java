package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.edit.dto.RequestEditDetailResponseDTO;
import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.edit.repository.EditRequestRepository;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.mypage.repository.PortfolioRepository;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.entity.UserRole;
import com.kkks.pofolling.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EditResponseServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private EditRequestRepository editRequestRepository;
    @Autowired
    private EditResponseService editResponseService;

    @Test
    @DisplayName("요청첨삭_세부정보_가져오기")
    void getRequestEditDetail() {
        //given
        User user = createTestUser();
        User mentee = userRepository.save(user);
        Portfolio portfolio = createTestPf(mentee);
        Portfolio pf = portfolioRepository.save(portfolio);
        EditRequest editRequest = createTestER(pf, mentee,null);
        EditRequest er = editRequestRepository.save(editRequest);

        //when
        RequestEditDetailResponseDTO requestEditDetail = editResponseService.getRequestEditDetail(er.getEditRequestId());

        //then
        assertThat(requestEditDetail.getPortfolioId()).isEqualTo(pf.getPortfolioId());
        assertThat(requestEditDetail.getTitle()).isEqualTo(pf.getTitle());
        assertThat(requestEditDetail.getFileUrl()).isEqualTo(pf.getFileUrl());
        assertThat(requestEditDetail.getNickname()).isEqualTo(mentee.getNickname());
        assertThat(requestEditDetail.getProfileImage()).isEqualTo(mentee.getProfileImage());
    }


    //==테스트용 요청첨삭 생성 메서드==//
    private EditRequest createTestER(Portfolio pf, User mentee, User mentor) {
        return EditRequest.builder()
                .portfolio(pf)
                .mentee(mentee)
                .mentor(mentor)
                .build();
    }

    //==테스트용 포폴 생성 메서드==//
    private Portfolio createTestPf(User user) {
        return Portfolio.builder()
                .title("포폴 테스트")
                .content("테스트입니다.")
                .user(user)
                .status(PortfolioStatus.REQUESTED)
                .build();
    }

    //==테스트용 유저 생성 메서드==//
    private User createTestUser() {
        return User.builder()
                .email("test@example.com")
                .password("1234")
                .name("테스트")
                .nickname("tester")
                .role(UserRole.MENTEE)
                .isVerified(false)
                .build();
    }
}