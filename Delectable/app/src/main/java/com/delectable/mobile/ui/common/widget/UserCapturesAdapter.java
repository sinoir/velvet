package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.profile.widget.CaptureSimpleItemRow;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class UserCapturesAdapter extends BaseAdapter {

    private Activity mContext;

    private ArrayList<CaptureDetails> mData;

    private String mUserId;

    public UserCapturesAdapter(Activity context, ArrayList<CaptureDetails> data, String userId) {
        mContext = context;
        mData = data;
        mUserId = userId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getSimpleListingView(position, convertView, parent);
        return view;
    }

    public View getSimpleListingView(int position, View convertView, ViewGroup parent) {
        CaptureSimpleItemRow rowView = (CaptureSimpleItemRow) convertView;
        CaptureDetails capture = mData.get(position);

        if (rowView == null) {
            rowView = new CaptureSimpleItemRow(mContext);
        }
        rowView.updateData(capture, mUserId);
        return rowView;
    }
}
