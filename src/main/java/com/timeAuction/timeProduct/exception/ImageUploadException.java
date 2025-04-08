package com.timeAuction.timeProduct.exception;

public class ImageUploadException extends MainException{

    private final static String MESSAGE = "이미지 업로드중 오류발생";

    public ImageUploadException(){super(MESSAGE);}

    @Override
    public int getStatusCode() {
        return 400;
    }
}
