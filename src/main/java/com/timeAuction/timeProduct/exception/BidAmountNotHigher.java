package com.timeAuction.timeProduct.exception;

public class BidAmountNotHigher extends MainException{

    private final static String MESSAGE = "입찰 금액은 현재 최고 입찰 금액보다 커야합니다.";

    public BidAmountNotHigher (){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
