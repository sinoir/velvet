package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.events.BaseEvent;

import java.util.ArrayList;

public class FetchInfluencerSuggestionsEvent extends BaseEvent{

    private ArrayList<AccountMinimal> mAccounts;

    public FetchInfluencerSuggestionsEvent(String errorMessage) {
        super(errorMessage);
    }

    public FetchInfluencerSuggestionsEvent(ArrayList<AccountMinimal> accounts) {
        super(true);
        mAccounts = accounts;
    }

    public ArrayList<AccountMinimal> getAccounts() {
        return mAccounts;
    }

}
