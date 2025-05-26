package com.kkks.pofolling.exception;

import lombok.RequiredArgsConstructor;

public enum ExceptionCode {

    // user
    MENTOR_NOT_FOUND(404, "MENTOR_NOT_FOUND", "해당 멘토가 존재하지 않습니다."),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "해당 사용자가 존재하지 않습니다."),
    NICKNAME_ALREADY_EXISTS(400, "NICKNAME_ALREADY_EXISTS", "이미 사용 중인 닉네임입니다."),

    // mypage-profile
    MYPAGE_CAREER_NOT_FOUND(404, "MYPAGE_CAREER_NOT_FOUND", "해당 경력 정보가 존재하지 않습니다."),
    MYPAGE_EDUCATION_NOT_FOUND(404, "MYPAGE_EDUCATION_NOT_FOUND", "해당 학력 정보가 존재하지 않습니다."),

    // portfolio
    PORTFOLIO_NOT_FOUND(404, "PORTFOLIO_NOT_FOUND", "해당 포트폴리오가 존재하지 않습니다."),
    INVALID_PORTFOLIO_STATUS(400, "INVALID_PORTFOLIO_STATUS", "첨삭 요청 상태인 포트폴리오만 첨삭을 시작할 수 있습니다."),
    PORTFOLIO_CANNOT_BE_MODIFIED(400, "PORTFOLIO_CANNOT_BE_MODIFIED", "현재 상태에서는 포트폴리오를 수정할 수 없습니다."),
    PORTFOLIO_CANNOT_BE_DELETED(400, "PORTFOLIO_CANNOT_BE_DELETED", "현재 상태에서는 포트폴리오를 삭제할 수 없습니다."),
    INVALID_INPUT_VALUE(400, "INVALID_INPUT_VALUE", "필수 입력 항목을 모두 입력해 주세요."),

    // community
    POST_NOT_FOUND(400,"POST_NOT_FOUND","해당 게시글이 존재하지 않습니다."),
    FILE_UPLOAD_FAILED(400,"FILE_UPLOAD_FAILED","파일 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(400, "FILE_DELETE_FAILED", "S3 파일 삭제 중 오류 발생."),
    UNAUTHORIZED_POST(403, "UNAUTHORIZED_POST", "해당 게시글의 소유자가 아닙니다"),
    UNAUTHORIZED_REPLY(403, "UNAUTHORIZED_REPLY", "해당 댓글의 소유자가 아닙니다"),
    REPLY_NOT_FOUND(400, "REPLY_NOT_FOUND", "해당 댓글이 존재하지 않습니다."),
    FILE_SLOT_FULL(400,"FILE_SLOT_FULL","파일 첨부 가능 개수를 초과하셨습니다."),

    // chat
    CHATROOM_NOT_FOUND(404, "CHATROOM_NOT_FOUND", "해당 채팅방이 존재하지 않습니다."),
    CHATROOM_CLOSED(403, "CHATROOM_CLOSED", "첨삭이 종료된 채팅방입니다."),

    // edit
    INVALID_EDIT_STATE(400,"INVALID_EDIT_STATE","등록 상태인 프트폴리오만 첨삭을 요청할 수 있습니다."),
    UNAUTHORIZED_EDIT_REQUEST(403,"UNAUTHORIZED_EDIT_REQUEST","해당 포트폴리오의 소유자가 아닙니다."),
    EDIT_NOT_FOUND(404,"EDIT_NOT_FOUND","해당 첨삭 요청이 존재하지 않습니다."),
    ALREADY_ASSIGNED_MENTOR(403,"ALREADY_ASSIGNED_MENTOR","이미 다른 멘토가 첨삭 중입니다."),
    NOT_VERIFIED_MENTOR(400,"NOT_VERIFIED_MENTOR","검증되지 않은 멘토입니다."),

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
