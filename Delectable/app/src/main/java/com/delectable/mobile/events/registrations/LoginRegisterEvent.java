package com.delectable.mobile.events.registrations;

import com.delectable.mobile.events.BaseEvent;

public class LoginRegisterEvent extends BaseEvent {

    public LoginRegisterEvent(boolean successful) {
        super(successful);
    }

    public LoginRegisterEvent(String errorMessage) {
        super(errorMessage);
    }
}
