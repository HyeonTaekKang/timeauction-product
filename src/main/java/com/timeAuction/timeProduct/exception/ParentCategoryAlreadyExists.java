package com.timeAuction.timeProduct.exception;


public class ParentCategoryAlreadyExists extends MainException{
    private final static String MESSAGE = "이미 존재하는 대분류입니다.";

    public ParentCategoryAlreadyExists(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
