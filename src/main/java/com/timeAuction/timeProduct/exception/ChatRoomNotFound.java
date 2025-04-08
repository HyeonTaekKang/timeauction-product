package com.timeAuction.timeProduct.exception;

public class ChatRoomNotFound extends MainException{

    private final static String MESSAGE = "존재하지 않는 채팅방입니다.";

    public ChatRoomNotFound(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 404;
    }
}
