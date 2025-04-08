package com.timeAuction.timeProduct.exception;

public class NotOwnProduct extends MainException{

    private final static String MESSAGE = "본인의 판매상품이 아닙니다.";

    public NotOwnProduct(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 403;
    }
}
