package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.events.BaseEvent;

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
