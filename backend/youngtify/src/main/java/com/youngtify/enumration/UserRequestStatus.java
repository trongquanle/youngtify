package com.youngtify.enumration;

public enum UserRequestStatus {
    REQUEST(1),
    ACCEPT(2),
    REJECT(3);

    private final int value;

    UserRequestStatus(int value) {
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

    public static UserRequestStatus getStatus(int value){
        for (UserRequestStatus u : UserRequestStatus.values()){
            if (u.value == value)
                return u;
        }
        return null;
    }
}
