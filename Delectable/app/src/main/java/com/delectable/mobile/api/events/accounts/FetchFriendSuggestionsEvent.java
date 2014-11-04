package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.util.ErrorUtil;

import java.util.ArrayList;

/**
 * This event occurs for these endpoints: /accounts/influencer_suggestions
 * /accounts/facebook_suggestions
 */
public class FetchFriendSuggestionsEvent extends BaseEvent {


    private String mId;

    private ArrayList<AccountMinimal> mAccounts;

    public FetchFriendSuggestionsEvent(String id, String errorMessage, ErrorUtil errorCode) {
        super(errorMessage, errorCode);
        mId = id;
    }

    public FetchFriendSuggestionsEvent(String id, ArrayList<AccountMinimal> accounts) {
        super(true);
        mId = id;
        mAccounts = accounts;
    }

    public String getId() {
        return mId;
    }

    public ArrayList<AccountMinimal> getAccounts() {
        return mAccounts;
    }

}
