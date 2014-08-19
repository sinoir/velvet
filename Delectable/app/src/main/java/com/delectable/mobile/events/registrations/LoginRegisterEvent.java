package com.delectable.mobile.events.registrations;

public class LoginRegisterEvent {

    private boolean mSuccessful;

    private String mErrorMessage;

    public LoginRegisterEvent(boolean successful) {
        mSuccessful = successful;
    }

    public LoginRegisterEvent(String errorMessage) {
        mSuccessful = false;
        mErrorMessage = errorMessage;
    }

    public boolean isSuccessful() {
        return mSuccessful;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

}
