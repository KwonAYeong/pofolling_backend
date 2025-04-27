package com.kkks.pofolling.edit.entity;

import com.kkks.pofolling.exception.BusinessException;
import com.kkks.pofolling.exception.ExceptionCode;
import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "edit_request")
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@EntityListeners(AuditingEntityListener.class)
public class EditRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edit_request_id")
    private Long editRequestId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee_id", nullable = false)
    private User mentee;

    @CreatedDate
    @Column(name = "requested_at", updatable = false)
    private LocalDateTime requestedAt;

    //==객체 생성 메서드==//
    public static EditRequest create(Portfolio portfolio, User mentee) {
        return EditRequest.builder()
                .portfolio(portfolio)
                .mentee(mentee)
                .requestedAt(LocalDateTime.now())
                .build();
    }


    //==멘토 검증 및 배정 메서드==//
    public void assignMentor(User mentor) {
        //멘토 검증 로직
        Boolean isVerified = mentor.getIsVerified();
        if (!isVerified) {
            throw new BusinessException(ExceptionCode.NOT_VERIFIED_MENTOR);
        }

        //멘토 유무
        if (this.mentor != null) {
            throw new BusinessException(ExceptionCode.ALREADY_ASSIGNED_MENTOR);
        }
        this.mentor = mentor;
    }

}
