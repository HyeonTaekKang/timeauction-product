package com.timeAuction.timeProduct.exception;

public class UserChatRoomNotFound extends MainException{

    private final static String MESSAGE = "해당 유저가 참가중인 채팅방이 존재하지 않습니다.";

    public UserChatRoomNotFound(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
