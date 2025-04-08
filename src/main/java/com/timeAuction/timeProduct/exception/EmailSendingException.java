package com.timeAuction.timeProduct.exception;

public class EmailSendingException extends MainException{

    private final static String MESSAGE = "메일 전송실패";

    public EmailSendingException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
