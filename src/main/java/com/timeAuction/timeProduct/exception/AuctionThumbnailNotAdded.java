package com.timeAuction.timeProduct.exception;

public class AuctionThumbnailNotAdded extends MainException{

    private final static String MESSAGE = "경매의 대표이미지가 제대로 추가되지 않음.";

    public AuctionThumbnailNotAdded(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
