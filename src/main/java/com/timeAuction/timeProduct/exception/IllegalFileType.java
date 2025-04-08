package com.timeAuction.timeProduct.exception;

public class IllegalFileType extends MainException{

    private final static String MESSAGE = "허용되지 않는 파일타입";

    public IllegalFileType(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }

}
