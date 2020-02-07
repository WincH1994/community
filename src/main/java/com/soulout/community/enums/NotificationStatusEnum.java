package com.soulout.community.enums;

public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1);

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    private int status;
}
