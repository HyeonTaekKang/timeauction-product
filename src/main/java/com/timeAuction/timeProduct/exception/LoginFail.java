package com.timeAuction.timeProduct.exception;

public class LoginFail extends MainException{

    private final static String MESSAGE = "아이디 또는 비밀번호를 다시 확인해주세요";

    public LoginFail(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 401;
    }
}
