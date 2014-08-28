package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;

import org.apache.commons.lang3.Range;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAccountsMinimalAdapter {

    private static final String TAG = ContactsAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ACCOUNT = 1;

    private static final int TYPE_CONTACT = 2;

    private List<TaggeeContact> mContacts = new ArrayList<TaggeeContact>();

    // Stores the range where the position for Account objects are in the list view
    private Range<Integer> mAccountPositionRange;

    // Stores the range where the position for Contact objects are in the list view
    private Range<Integer> mContactPositionRange;

    public ContactsAdapter(FollowActionsHandler actionsHandler) {
        super(actionsHandler);
        updateRanges();
    }

    @Override
    public void setAccounts(ArrayList<AccountMinimal> accounts) {
        super.setAccounts(accounts);
        updateRanges();
    }

    public void setContacts(List<TaggeeContact> contacts) {
        mContacts = contacts;
        updateRanges();
    }

    /**
     * Update Ranges that contains the position of each sections
     *
     * We have 2 fixed headers + 2 sections
     */
    private void updateRanges() {
        // Position for offset for show contacts , 2 headers + num accounts
        int contactSectionStartOffset = 2;
        if (mAccounts.size() == 0) {
            // Negative range for no Accounts
            mAccountPositionRange = Range.between(-1, -1);
            // First offset is for the 2 headers
        } else {
            mAccountPositionRange = Range.between(1, mAccounts.size());
            contactSectionStartOffset += mAccountPositionRange.getMaximum();
        }

        if (mContacts.size() == 0) {
            // Negative range for no Contacts
            mContactPositionRange = Range.between(-1, -1);
        } else {
            // Skip 1 for the Contacts header
            mContactPositionRange = Range.between(contactSectionStartOffset,
                    mContacts.size() + contactSectionStartOffset);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mAccountPositionRange.contains(position)) {
            return TYPE_ACCOUNT;
        }
        if (mContactPositionRange.contains(position)) {
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
                return mAccounts.get(position - mAccountPositionRange.getMinimum());
            case TYPE_CONTACT:
                return mContacts.get(position - mContactPositionRange.getMinimum());
        }
        // Otherwise it's an Invite Header:
        if (position != 0) {
            return Integer.valueOf(R.string.follow_friends_invite_to_delectable);
        }
        return Integer.valueOf(R.string.follow_friends_contacts);
    }

    @Override
    public int getCount() {
        // 2 headers + size of Accounts and Contacts
        return 2 + mAccounts.size() + mContacts.size();
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

    private View getFollowRow(int position, View convertView, ViewGroup parent) {
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

    private View getHeaderRow(int position, View convertView, ViewGroup parent) {
        TextView row = (TextView) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (TextView) inflater
                    .inflate(R.layout.header_find_people, parent, false);
        }
        row.setText((Integer) getItem(position));

        return row;
    }
}
