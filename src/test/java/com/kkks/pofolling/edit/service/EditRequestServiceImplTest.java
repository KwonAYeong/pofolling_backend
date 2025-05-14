package com.kkks.pofolling.edit.service;

import com.kkks.pofolling.edit.entity.EditRequest;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.mypage.repository.PortfolioRepository;
import com.kkks.pofolling.user.entity.User;
import com.kkks.pofolling.user.entity.UserRole;
import com.kkks.pofolling.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class EditRequestServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private EditRequestService editRequestService;

    @Test
    void 등록된첨삭_요청() {
        //given
        User user = userRepository.save(createTestUser());
        Portfolio pf = Portfolio.builder()
                .title("포폴 테스트")
                .content("테스트입니다.")
                .user(user)
                .status(PortfolioStatus.REGISTERED)
                .build();

        //when
        Portfolio savePf = portfolioRepository.save(pf);
        //editRequestService.requestEdit(savePf.getPortfolioId());
        Optional<Portfolio> result = portfolioRepository.findById(savePf.getPortfolioId());

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(PortfolioStatus.REQUESTED);
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


}