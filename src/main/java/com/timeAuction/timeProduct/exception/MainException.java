package com.timeAuction.timeProduct.exception;



public abstract class MainException extends RuntimeException{

    public MainException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
