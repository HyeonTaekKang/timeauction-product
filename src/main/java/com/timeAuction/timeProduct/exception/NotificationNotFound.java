package com.timeAuction.timeProduct.exception;

public class NotificationNotFound extends MainException{

    private final static String MESSAGE = "해당알림이 존재하지 않음.";

    public NotificationNotFound(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
