package com.timeAuction.timeProduct.exception;

public class PostCommentNotFound extends MainException{

    private final static String MESSAGE = "댓글이 존재하지 않습니다.";

    public PostCommentNotFound(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
