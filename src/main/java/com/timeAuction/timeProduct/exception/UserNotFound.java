package com.timeAuction.timeProduct.exception;

public class UserNotFound extends MainException{

    private final static String MESSAGE = "존재하지 않는 회원입니다.";

    public UserNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}

