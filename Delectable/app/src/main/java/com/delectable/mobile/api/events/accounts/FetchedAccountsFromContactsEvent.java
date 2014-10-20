package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;

import java.util.ArrayList;
import java.util.List;

public class FetchedAccountsFromContactsEvent extends BaseEvent {

    private ArrayList<AccountMinimal> mAccounts;

    private List<TaggeeContact> mContacts;
    // TODO : Pass up Taggee Contacts that haven't signed up

    public FetchedAccountsFromContactsEvent(String errorMessage) {
        super(errorMessage);
    }

    public FetchedAccountsFromContactsEvent(ArrayList<AccountMinimal> accounts,
            List<TaggeeContact> contacts) {
        super(true);
        mAccounts = accounts;
        mContacts = contacts;
    }

    public ArrayList<AccountMinimal> getAccounts() {
        return mAccounts;
    }

    public List<TaggeeContact> getContacts() {
        return mContacts;
    }
}
