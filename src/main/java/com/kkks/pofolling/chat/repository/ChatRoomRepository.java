package com.kkks.pofolling.chat.repository;

import com.kkks.pofolling.chat.entity.ChatRoom;
import com.kkks.pofolling.mypage.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findByMentor_UserIdOrMentee_UserId(Long mentorId, Long menteeId);
    List<ChatRoom> findByMentor_UserIdAndMentee_UserIdAndIsActiveTrue(Long mentorId, Long menteeId);

}
