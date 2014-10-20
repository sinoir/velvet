package com.delectable.mobile.api.events;

import com.delectable.mobile.api.util.ErrorUtil;

public class BaseEvent {

    private boolean mIsSuccessful;

    private String mErrorMessage;

    private ErrorUtil mErrorCode;

    public BaseEvent(boolean isSuccessful) {
        mIsSuccessful = isSuccessful;
    }

    public BaseEvent(String errorMessage, ErrorUtil errorCode) {
        this(errorMessage);
        mErrorCode = errorCode;
    }

    public BaseEvent(String errorMessage) {
        mErrorMessage = errorMessage;
        mIsSuccessful = false;
    }

    public boolean isSuccessful() {
        return mIsSuccessful;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public ErrorUtil getErrorCode() {
        return mErrorCode;
    }
}
