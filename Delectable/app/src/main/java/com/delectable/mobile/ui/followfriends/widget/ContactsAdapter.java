package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactsAdapter extends BaseAccountsMinimalAdapter {

    public ContactsAdapter(FollowActionsHandler actionsHandler) {
        super(actionsHandler);
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
