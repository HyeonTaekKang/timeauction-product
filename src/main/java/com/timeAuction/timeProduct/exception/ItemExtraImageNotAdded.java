package com.timeAuction.timeProduct.exception;

public class ItemExtraImageNotAdded extends MainException{

    private final static String MESSAGE = "상품의 추가 이미지가 제대로 첨부되지 않음.";

    public ItemExtraImageNotAdded(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}