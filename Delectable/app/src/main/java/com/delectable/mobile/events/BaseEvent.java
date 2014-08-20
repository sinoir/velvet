package com.delectable.mobile.events;

public class BaseEvent {

    private boolean mIsSuccessful;

    private String mErrorMessage;

    public BaseEvent(boolean isSuccessful) {
        mIsSuccessful = isSuccessful;
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
}
