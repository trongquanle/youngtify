package com.youngtify.enumration;

public enum FriendStatus {
    FRIEND(1),
    BLOCK(2),
    UNFIREND(3),
    UNBLOCK(4);

    private final int value;

    FriendStatus(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
