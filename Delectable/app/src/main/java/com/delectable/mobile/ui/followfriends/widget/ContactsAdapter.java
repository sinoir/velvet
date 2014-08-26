package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ContactsAdapter extends BaseAdapter {

    private ArrayList<AccountMinimal> mAccounts = new ArrayList<AccountMinimal>();

    private FollowContactRow.ActionsHandler mActionsHandler;

    public ContactsAdapter(FollowContactRow.ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setAccounts(ArrayList<AccountMinimal> accounts) {
        mAccounts = accounts;
    }

    @Override
    public int getCount() {
        return mAccounts.size();
    }

    @Override
    public AccountMinimal getItem(int position) {
        return mAccounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FollowContactRow row = (FollowContactRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (FollowContactRow) inflater
                    .inflate(R.layout.row_find_contact_with_sizing, parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        row.updateData(getItem(position));

        return row;
    }
}
