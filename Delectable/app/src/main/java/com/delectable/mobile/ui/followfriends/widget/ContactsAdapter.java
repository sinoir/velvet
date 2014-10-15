package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAccountsMinimalAdapter {

    private static final String TAG = ContactsAdapter.class.getSimpleName();

    private static final int TYPE_CONTACT = 2;

    private List<TaggeeContact> mContacts = new ArrayList<TaggeeContact>();

    public ContactsAdapter(FollowActionsHandler actionsHandler) {
        super(actionsHandler);
    }

    @Override
    public void setAccounts(ArrayList<AccountMinimal> accounts) {
        super.setAccounts(accounts);
    }

    public void setContacts(List<TaggeeContact> contacts) {
        mContacts = contacts;
    }

    private boolean isTypeContact(int position) {
        int firstContactRowPosition = mAccounts.size() + getNumHeaders();
        return position >= firstContactRowPosition;
    }

    private boolean isTypeHeader(int position) {
        return position == 0 || position == mAccounts.size() + 1;
    }

    private boolean isTypeAccount(int position) {
        return position > 0 && position <= mAccounts.size();
    }

    private int getAccountOffsetPosition(int position) {
        return position - 1;
    }

    private int getContactOffsetPosition(int position) {
        return position - (mAccounts.size() + getNumHeaders());
    }

    /**
     * Get # of headers,
     *
     * @return - 1 or higher.  Top Header is required. this is meant for if 2nd header is displayed
     */
    private int getNumHeaders() {
        // If no Contacts exist, dont' show the Invite header
        if (mContacts == null || mContacts.size() == 0) {
            return 1;
        }
        return 2;
    }


    @Override
    public int getItemViewType(int position) {
        if (isTypeAccount(position)) {
            return TYPE_ACCOUNT;
        }
        if (isTypeContact(position)) {
            return TYPE_CONTACT;
        }
        return TYPE_HEADER;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        switch (getItemViewType(position)) {
            case TYPE_ACCOUNT:
                // Accounts is offset by beginning of range
                return mAccounts.get(getAccountOffsetPosition(position));
            case TYPE_CONTACT:
                return mContacts.get(getContactOffsetPosition(position));
        }
        // Otherwise it's an Invite Header:
        if (position != 0) {
            return Integer.valueOf(R.string.follow_friends_invite_to_delectable);
        }
        return getTopHeaderTitleResId();
    }

    @Override
    public int getCount() {
        // # headers + size of Accounts and Contacts
        if (mAccounts.isEmpty() && mContacts.isEmpty()) {
            return 0;
        } else {
            return getNumHeaders() + mAccounts.size() + mContacts.size();
        }
    }

    @Override
    public int getViewTypeCount() {
        // 3 Types: Header, Follow, Invite
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_ACCOUNT:
                return getFollowRow(position, convertView, parent);
            case TYPE_CONTACT:
                return getInviteRow(position, convertView, parent);
            case TYPE_HEADER:
                return getHeaderRow(position, convertView, parent);
        }

        return null;
    }

    @Override
    protected View getFollowRow(int position, View convertView, ViewGroup parent) {
        FollowContactRow row = (FollowContactRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (FollowContactRow) inflater
                    .inflate(R.layout.row_find_contact_with_sizing, parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        row.updateData((AccountMinimal) getItem(position));

        return row;
    }

    private View getInviteRow(int position, View convertView, ViewGroup parent) {
        InviteContactRow row = (InviteContactRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (InviteContactRow) inflater.inflate(R.layout.row_invite_contact_with_sizing,
                    parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        row.updateData((TaggeeContact) getItem(position));

        return row;
    }
}
