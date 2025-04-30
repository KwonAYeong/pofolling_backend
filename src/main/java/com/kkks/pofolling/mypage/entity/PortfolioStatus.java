package com.kkks.pofolling.mypage.entity;

public enum PortfolioStatus {
    REGISTERED,
    REQUESTED,
    IN_PROGRESS,
    COMPLETED;

    //==첨삭 가능한 상태값 확인 메서드==//
    public boolean isReRegistrable() {
        return this == REGISTERED || this == COMPLETED;
    }
}
