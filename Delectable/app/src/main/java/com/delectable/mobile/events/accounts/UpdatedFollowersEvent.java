package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.events.BaseEvent;

import java.util.ArrayList;

public class UpdatedFollowersEvent extends BaseEvent {

    private String mAccountId;

    private ArrayList<AccountMinimal> mFollowers;

    public UpdatedFollowersEvent(String accountId, ArrayList<AccountMinimal> followers) {
        super(true);
        mAccountId = accountId;
        mFollowers = followers;
    }
    public UpdatedFollowersEvent(String errorMessage) {
        super(errorMessage);
    }


    public String getAccountId() {
        return mAccountId;
    }

    public ArrayList<AccountMinimal> getFollowers() {
        return mFollowers;
    }
}
