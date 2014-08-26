package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.events.BaseEvent;

import java.util.ArrayList;

public class FetchedAccountsFromContactsEvent extends BaseEvent {

    private ArrayList<AccountMinimal> mAccounts;
    // TODO : Pass up Taggee Contacts that haven't signed up

    public FetchedAccountsFromContactsEvent(String errorMessage) {
        super(errorMessage);
    }

    public FetchedAccountsFromContactsEvent(ArrayList<AccountMinimal> accounts) {
        super(true);
        mAccounts = accounts;
    }

    public ArrayList<AccountMinimal> getAccounts() {
        return mAccounts;
    }

}
