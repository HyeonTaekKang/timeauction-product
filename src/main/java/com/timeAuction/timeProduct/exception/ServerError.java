package com.timeAuction.timeProduct.exception;

public class ServerError extends MainException{

    private final static String MESSAGE = "오류가 발생했습니다.";

    public ServerError(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
