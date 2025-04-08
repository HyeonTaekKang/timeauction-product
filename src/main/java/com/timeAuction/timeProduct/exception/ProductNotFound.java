package com.timeAuction.timeProduct.exception;

public class ProductNotFound extends MainException{
    private final static String MESSAGE = "존재하지 않는 상품입니다.";

    public ProductNotFound(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
