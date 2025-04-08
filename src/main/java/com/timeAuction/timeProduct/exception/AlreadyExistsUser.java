package com.timeAuction.timeProduct.exception;

public class AlreadyExistsUser extends MainException{

    private final static String MESSAGE = "이미 존재하는 유저입니다";

    public AlreadyExistsUser(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
