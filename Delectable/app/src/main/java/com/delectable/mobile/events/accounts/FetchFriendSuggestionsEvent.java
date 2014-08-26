package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.events.BaseEvent;

import java.util.ArrayList;

/**
 * This event occurs for these endpoints:
 * /accounts/influencer_suggestions
 * /accounts/facebook_suggestions
 */
public class FetchFriendSuggestionsEvent extends BaseEvent{

    private ArrayList<AccountMinimal> mAccounts;

    public FetchFriendSuggestionsEvent(String errorMessage) {
        super(errorMessage);
    }

    public FetchFriendSuggestionsEvent(ArrayList<AccountMinimal> accounts) {
        super(true);
        mAccounts = accounts;
    }

    public ArrayList<AccountMinimal> getAccounts() {
        return mAccounts;
    }

}
