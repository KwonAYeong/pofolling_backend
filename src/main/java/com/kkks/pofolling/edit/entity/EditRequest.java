package com.kkks.pofolling.edit.entity;

import com.kkks.pofolling.mypage.entity.Portfolio;
import com.kkks.pofolling.mypage.entity.PortfolioStatus;
import com.kkks.pofolling.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @ManyToOne (fetch = FetchType.LAZY)
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
        if (this.mentor != null) {
            throw new IllegalStateException("이미 다른 멘토가 지정되어 있습니다.");
        }
        this.mentor = mentor;
    }

}
