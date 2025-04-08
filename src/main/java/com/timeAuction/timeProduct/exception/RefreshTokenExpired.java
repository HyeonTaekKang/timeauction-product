package com.timeAuction.timeProduct.exception;

public class RefreshTokenExpired extends MainException{

    private final static String MESSAGE = "리프레시 토큰 만료 또는 잘못된 리프레시 토큰";

    public RefreshTokenExpired(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 401;
    }
}
