package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.util.ErrorUtil;

public class FollowAccountEvent extends BaseEvent {

    private String mAccountId;

    public FollowAccountEvent(String id, boolean isSuccessful) {
        super(isSuccessful);
        mAccountId = id;
    }

    public FollowAccountEvent(String id, String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mAccountId = id;
    }

    public String getAccountId() {
        return mAccountId;
    }
}
