package com.example.mylibrary;


public class ScriptoException extends RuntimeException {

    public ScriptoException() {
    }

    public ScriptoException(String detailMessage) {
        super(detailMessage);
    }


    public ScriptoException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
