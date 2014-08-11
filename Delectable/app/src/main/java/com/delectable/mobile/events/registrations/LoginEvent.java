package com.delectable.mobile.events.registrations;

public class LoginEvent {

    private boolean mSuccessful;

    public LoginEvent(boolean successful) {
        mSuccessful = successful;
    }

    public boolean isSuccessful() {
        return mSuccessful;
    }

}
