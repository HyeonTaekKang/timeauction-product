package com.timeAuction.timeProduct.exception;

public class PostNotFound extends MainException{
    private final static String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
