package com.timeAuction.timeProduct.exception;

public class SessionNotFoundException extends MainException{
    private final static String MESSAGE = "세션을 찾을 수 없음";

    public SessionNotFoundException(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
