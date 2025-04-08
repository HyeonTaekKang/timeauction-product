package com.timeAuction.timeProduct.exception;

public class ExpiredAuction extends MainException{

    private final static String MESSAGE = "이미 만료된 경매입니다.";

    public ExpiredAuction(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
