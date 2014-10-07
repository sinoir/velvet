package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TwitterAccountsAdapter extends BaseAccountsMinimalAdapter {

    public TwitterAccountsAdapter(FollowActionsHandler actionsHandler) {
        super(actionsHandler);
    }

    @Override
    protected View getFollowRow(int position, View convertView, ViewGroup parent) {
        FollowTwitterFriendsRow row = (FollowTwitterFriendsRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (FollowTwitterFriendsRow) inflater
                    .inflate(R.layout.row_find_twitter_friends_impl, parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        row.updateData((com.delectable.mobile.api.models.AccountMinimal) getItem(position));

        return row;
    }
}
