package com.delectable.mobile.ui.tagpeople.widget;

import com.delectable.mobile.api.models.AccountMinimal;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class TagPeopleAdapter extends BaseAdapter {

    private ArrayList<AccountMinimal> mDelectaFriends;

    // TODO: Extend with FB Friends
    public TagPeopleAdapter(ArrayList<AccountMinimal> delectaFriends) {
        mDelectaFriends = delectaFriends;
    }

    @Override
    public int getCount() {
        return mDelectaFriends.size();
    }

    @Override
    public AccountMinimal getItem(int position) {
        return mDelectaFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TagPeopleRow row = (TagPeopleRow) convertView;
        if (row == null) {
            row = new TagPeopleRow(parent.getContext());
            AccountMinimal account = getItem(position);
            row.updateData(account.getFullName());
        }
        return row;
    }
}
