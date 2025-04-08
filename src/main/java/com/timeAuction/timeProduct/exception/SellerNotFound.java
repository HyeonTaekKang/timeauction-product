package com.timeAuction.timeProduct.exception;


public class SellerNotFound extends MainException{

    private final static String MESSAGE = "존재하지 않는 판매자입니다.";

    public SellerNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}

