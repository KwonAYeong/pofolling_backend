-- users
INSERT INTO users (user_id, email, password, name, nickname, role, is_verified)
VALUES
(1, 'kay@example.com', '1234', 'KAY', 'kay', 'MENTEE', false),
(2, 'khg@example.com', '1234', 'KHG', 'khg', 'MENTEE', false),
(3, 'khm@example.com', '1234', 'KHM', 'khm', 'MENTOR', false),
(4, 'sjy@example.com', '1234', 'SJY', 'sjy', 'MENTOR', false);

-- portfolio
INSERT INTO portfolio (portfolio_id, user_id, title, content, file_url, status, created_at, updated_at)
VALUES
(1, 1, '포트폴리오 제목1', '포트폴리오 내용1', null, 'REQUESTED', now(), now()),
(2, 1, '포트폴리오 제목2', '포트폴리오 내용2', null, 'REQUESTED', now(), now()),
(3, 2, '포트폴리오 제목3', '포트폴리오 내용3', null, 'REGISTERED', now(), now()),
(4, 2, '포트폴리오 제목4', '포트폴리오 내용4', null, 'REGISTERED', now(), now());

-- chat_room
INSERT INTO chat_room (chat_room_id, portfolio_id, mentor_id, mentee_id, created_at, updated_at)
VALUES
(1, 1, 3, 1, NOW(), NOW()),
(2, 3, 4, 2, NOW(), NOW()),
(3, 2, 4, 1, NOW(), NOW());

-- chat_message
INSERT INTO chat_message (message_id, chat_room_id, sender_id, message, sent_at)
VALUES
-- 채팅방 1 (포트폴리오 1 - 멘티 1, 멘토 3)
(1, 1, 1, '안녕하세요, 포트폴리오 첨삭 부탁드립니다!', NOW()),
(2, 1, 3, '안녕하세요! 잘 봤습니다.', NOW()),
(3, 1, 3, '첫 번째 프로젝트에 기술 스택을 좀 더 적어주세요.', NOW()),
(4, 1, 1, '넵! 추가해보겠습니다.', NOW()),
(5, 1, 3, '좋습니다. 반영하고 알려주세요!', NOW()),

-- 채팅방 2 (포트폴리오 3 - 멘티 2, 멘토 4)
(6, 2, 2, '안녕하세요. 첨삭 요청드립니다.', NOW()),
(7, 2, 4, '포트폴리오 잘 봤습니다!', NOW()),
(8, 2, 4, '프로젝트 설명이 조금 부족한 것 같아요.', NOW()),
(9, 2, 2, '그 부분 더 보완하겠습니다.', NOW()),
(10, 2, 4, '네, 기대하겠습니다.', NOW()),

-- 채팅방 3 (포트폴리오 2 - 멘티 1, 멘토 4)
(11, 3, 1, '안녕하세요, 포트폴리오 추가 제출합니다.', NOW()),
(12, 3, 4, '확인했습니다!', NOW()),
(13, 3, 4, '전체적으로 잘 정리되어 있어요.', NOW()),
(14, 3, 1, '감사합니다!', NOW()),
(15, 3, 4, '추가 피드백 있으면 드릴게요.', NOW());
