package com.timeAuction.timeProduct.exception;

public class AlreadyExistsCategory extends MainException {
    private final static String MESSAGE = "이미 존재하는 카테고리입니다.";

    public AlreadyExistsCategory(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
