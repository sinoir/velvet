package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.api.models.Capture;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class FollowFeedAdapter extends BaseAdapter {

    private static final String TAG = "FollowFeedAdapter";

    private Activity mContext;

    private ArrayList<CaptureDetails> mData;

    private FeedItemActionsHandler mActionsHandler;

    private CaptureDetailsView.CaptureActionsHandler mCaptureActionsHandler;

    public FollowFeedAdapter(Activity context, ArrayList<CaptureDetails> data,
            FeedItemActionsHandler actionsHandler,
            CaptureDetailsView.CaptureActionsHandler captureActionsHandler) {
        mContext = context;
        mData = data;
        mActionsHandler = actionsHandler;
        mCaptureActionsHandler = captureActionsHandler;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Capture getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CaptureDetailsView rowView = (CaptureDetailsView) convertView;
        CaptureDetails capture = mData.get(position);

        if (rowView == null) {
            rowView = new CaptureDetailsView(mContext);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        rowView.updateData(capture);
        if (position == mData.size() - 1) {
            mActionsHandler.shouldLoadNextPage();
        }

        return rowView;
    }

    public static interface FeedItemActionsHandler {

        public void shouldLoadNextPage();
    }
}
