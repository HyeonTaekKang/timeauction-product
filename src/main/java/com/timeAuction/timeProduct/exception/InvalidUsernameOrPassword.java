package com.timeAuction.timeProduct.exception;

public class InvalidUsernameOrPassword extends MainException{

    private final static String MESSAGE = "아이디 또는 비밀번호가 올바르지 않습니다.";

    public InvalidUsernameOrPassword(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
