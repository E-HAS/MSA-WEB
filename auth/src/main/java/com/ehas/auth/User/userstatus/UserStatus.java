package com.ehas.auth.User.userstatus;

public enum UserStatus {
    ACTIVE(1),
    INACTIVE(2),
    ACCOUNT_EXPIRED(3),
    ACCOUNT_LOCKED(4),
    CREDENTIALS_EXPIRED(5);

    private final int statusCode;

    UserStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getInteger() {
        return statusCode;
    }

    public static UserStatus getString(int statusCode) {
        for (UserStatus status : UserStatus.values()) {
            if (status.statusCode == statusCode) return status;
        }
        throw new IllegalArgumentException("Unknown status: " + statusCode);
    }
}