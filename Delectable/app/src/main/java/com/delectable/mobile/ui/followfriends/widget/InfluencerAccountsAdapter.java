package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfluencerAccountsAdapter extends BaseAccountsMinimalAdapter {

    public InfluencerAccountsAdapter(FollowActionsHandler actionsHandler) {
        super(actionsHandler);
    }

    @Override
    protected View getFollowRow(int position, View convertView, ViewGroup parent) {
        FollowExpertsRow row = (FollowExpertsRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (FollowExpertsRow) inflater
                    .inflate(R.layout.row_find_experts_impl, parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        row.updateData((com.delectable.mobile.api.models.AccountMinimal) getItem(position));

        return row;
    }
}
