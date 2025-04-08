package com.timeAuction.timeProduct.exception;

public class ExpiredAuctionTicket extends MainException{

    private final static String MESSAGE = "유효기간이 지난 경매권을 보유중입니다.";

    public ExpiredAuctionTicket(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
