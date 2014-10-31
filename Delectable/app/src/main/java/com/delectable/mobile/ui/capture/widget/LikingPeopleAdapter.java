package com.delectable.mobile.ui.capture.widget;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.ui.common.widget.PeopleRow;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class LikingPeopleAdapter extends BaseAdapter {

    private List<AccountMinimal> mLikingParticipants;

    public LikingPeopleAdapter(List<AccountMinimal> likingParticipants) {
        mLikingParticipants = likingParticipants;
    }

    @Override
    public int getCount() {
        return mLikingParticipants.size();
    }

    @Override
    public AccountMinimal getItem(int position) {
        return mLikingParticipants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PeopleRow row = (PeopleRow) convertView;
        if (row == null) {
            row = new PeopleRow(parent.getContext());
        }
        AccountMinimal likingUser = getItem(position);
        row.updateData(likingUser.getPhoto().getBestThumb(), likingUser.getFullName());

        return row;
    }
}
