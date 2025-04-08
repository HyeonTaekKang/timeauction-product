package com.timeAuction.timeProduct.exception;

public class AlreadySoldProduct extends MainException{

    private final static String MESSAGE = "이미 판매가 종료된 상품입니다.";

    public AlreadySoldProduct(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 403;
    }
}
