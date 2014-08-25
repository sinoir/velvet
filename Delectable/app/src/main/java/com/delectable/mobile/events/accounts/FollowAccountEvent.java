package com.delectable.mobile.events.accounts;

import com.delectable.mobile.events.BaseEvent;

/**
 * Created by wayne on 8/22/14.
 */
public class FollowAccountEvent extends BaseEvent{

    private String mAccountId;

    public FollowAccountEvent(String id, boolean isSuccessful) {
        super(isSuccessful);
        mAccountId = id;
    }

    public FollowAccountEvent(String id, String errorMessage) {
        super(errorMessage);
        mAccountId = id;
    }

    public String getAccountId() {
        return mAccountId;
    }
}
