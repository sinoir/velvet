package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FollowersAdapter extends InfiniteScrollAdapter<AccountMinimal> {

    private FollowersRow.ActionsHandler mRowActionsHandler;

    public FollowersAdapter(ActionsHandler infiniteScrollHandler,
            FollowersRow.ActionsHandler rowActionsHandler) {
        super(infiniteScrollHandler);
        mRowActionsHandler = rowActionsHandler;
    }

    @Override
    public AccountMinimal getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        FollowersRow row = (FollowersRow) view;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (FollowersRow) inflater.inflate(R.layout.row_followers_impl, parent, false);
            row.setActionsHandler(mRowActionsHandler);
        }
        row.updateData(getItem(position));

        return row;
    }
}
