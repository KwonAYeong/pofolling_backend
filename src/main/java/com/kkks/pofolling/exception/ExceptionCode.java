package com.kkks.pofolling.exception;

import lombok.RequiredArgsConstructor;

public enum ExceptionCode {

    // user
    MENTOR_NOT_FOUND(404, "MENTOR_NOT_FOUND", "해당 멘토가 존재하지 않습니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "해당 사용자가 존재하지 않습니다."),

    // portfolio
    PORTFOLIO_NOT_FOUND(404, "PORTFOLIO_NOT_FOUND", "해당 포트폴리오가 존재하지 않습니다."),
    INVALID_PORTFOLIO_STATUS(400, "INVALID_PORTFOLIO_STATUS", "첨삭 요청 상태인 포트폴리오만 첨삭을 시작할 수 있습니다."),

    // chat
    CHATROOM_NOT_FOUND(404, "CHATROOM_NOT_FOUND", "해당 채팅방이 존재하지 않습니다."),
    CHATROOM_CLOSED(403, "CHATROOM_CLOSED", "첨삭이 종료된 채팅방입니다."),


    // system_error
    UNKNOWN_ERROR(500, "UNKNOWN_ERROR", "예상치 못한 서버 에러가 발생했습니다.");

    private final int status;
    private final String code;
    private final String message;

    ExceptionCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }

}
