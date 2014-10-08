package com.delectable.mobile.events.registrations;

import com.delectable.mobile.events.BaseEvent;

public class ResetPasswordEvent extends BaseEvent {

    public String mEmail;

    public ResetPasswordEvent(String email, boolean successful) {
        super(successful);
        mEmail = email;
    }

}
