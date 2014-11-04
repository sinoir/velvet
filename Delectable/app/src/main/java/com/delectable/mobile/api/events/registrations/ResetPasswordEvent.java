package com.delectable.mobile.api.events.registrations;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class ResetPasswordEvent extends BaseEvent {

    private String mEmail;

    public ResetPasswordEvent(String email, boolean successful) {
        super(successful);
        mEmail = email;
    }

    public ResetPasswordEvent(String email, String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }
}
