package com.timeAuction.timeProduct.exception;


public class AuctionNotFound extends MainException{

    private final static String MESSAGE = "존재하지 않는 경매입니다.";

    public AuctionNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
