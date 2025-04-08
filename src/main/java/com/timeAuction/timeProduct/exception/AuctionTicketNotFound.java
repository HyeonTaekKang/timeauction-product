package com.timeAuction.timeProduct.exception;

public class AuctionTicketNotFound extends MainException{

    private final static String MESSAGE = "경매권을 보유하고 있지 않습니다.";

    public AuctionTicketNotFound(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 200;
    }
}
