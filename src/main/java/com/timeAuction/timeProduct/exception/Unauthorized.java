package com.timeAuction.timeProduct.exception;

public class Unauthorized extends MainException{

    private final static String MESSAGE = "인증이 필요합니다";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
