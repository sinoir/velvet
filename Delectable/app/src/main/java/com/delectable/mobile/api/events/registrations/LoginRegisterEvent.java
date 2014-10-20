package com.delectable.mobile.api.events.registrations;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class LoginRegisterEvent extends BaseEvent {

    public LoginRegisterEvent(boolean successful) {
        super(successful);
    }

    public LoginRegisterEvent(String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
    }
}
