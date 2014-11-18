package com.delectable.mobile.ui.capture.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.common.widget.PeopleRow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class TaggedPeopleAdapter extends BaseAdapter {

    private CaptureDetails mCaptureDetails;

    private List<AccountMinimal> mTaggedParticipants;

    public TaggedPeopleAdapter(CaptureDetails captureDetails) {
        mCaptureDetails = captureDetails;
        mTaggedParticipants = captureDetails.getRegisteredParticipants();
    }

    @Override
    public int getCount() {
        return mTaggedParticipants.size();
    }

    @Override
    public AccountMinimal getItem(int position) {
        return mTaggedParticipants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PeopleRow row = (PeopleRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (PeopleRow) inflater.inflate(R.layout.row_people_impl, parent, false);
        }
        AccountMinimal likingUser = getItem(position);
        int rating = mCaptureDetails.getRatingForId(likingUser.getId());
        row.updateData(likingUser.getPhoto().getBestThumb(), likingUser.getFullName(),
                likingUser.isInfluencer(), rating);

        return row;
    }
}
