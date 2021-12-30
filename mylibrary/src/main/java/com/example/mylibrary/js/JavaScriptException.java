package com.example.mylibrary.js;


import com.example.mylibrary.ScriptoException;

public class JavaScriptException extends ScriptoException {

    public JavaScriptException() {
    }

    public JavaScriptException(String detailMessage) {
        super(detailMessage);
    }


    public JavaScriptException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
