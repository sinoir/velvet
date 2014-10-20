package com.delectable.mobile.api.util;

public class DelException extends Exception {

    private int mErrorCode;

    public DelException() {
    }

    public DelException(String detailMessage, int errorCode) {
        super(detailMessage);
        mErrorCode = errorCode;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
