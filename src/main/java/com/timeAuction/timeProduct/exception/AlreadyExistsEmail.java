package com.timeAuction.timeProduct.exception;

public class AlreadyExistsEmail extends MainException{

    private final static String MESSAGE = "이미 존재하는 이메일입니다";

    public AlreadyExistsEmail(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
