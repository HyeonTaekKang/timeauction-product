package com.timeAuction.timeProduct.exception;

public class CurrentlySomeoneBeingbid extends MainException{

    private final static String MESSAGE = "먼저 경매를 진행하고 있는 사람이 있습니다. 잠시만 기다려주세요.";

    public CurrentlySomeoneBeingbid(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
