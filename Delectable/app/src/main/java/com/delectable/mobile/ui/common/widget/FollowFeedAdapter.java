package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.api.models.Capture;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.profile.widget.CaptureSimpleItemRow;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class FollowFeedAdapter extends BaseAdapter {

    public static final int DISPLAY_STATE_SIMPLE = 0;

    public static final int DISPLAY_STATE_DETAILED = 1;

    private int mCurrentState = DISPLAY_STATE_DETAILED;

    private static final String TAG = "FollowFeedAdapter";

    private Activity mContext;

    private ArrayList<CaptureDetails> mData;

    private String mUserId;

    private FeedItemActionsHandler mActionsHandler;

    private CaptureDetailsView.CaptureActionsHandler mCaptureActionsHandler;

    /**
     * @param context               - Activity Context
     * @param data                  - Capture data
     * @param actionsHandler        - Actions to handle pagination stuff
     * @param captureActionsHandler - Capture Details View actions for liking / etc.
     * @param userId                - Selected specific users's capture listing
     */
    public FollowFeedAdapter(Activity context, ArrayList<CaptureDetails> data,
            FeedItemActionsHandler actionsHandler,
            CaptureDetailsView.CaptureActionsHandler captureActionsHandler, String userId) {
        mContext = context;
        mData = data;
        mActionsHandler = actionsHandler;
        mCaptureActionsHandler = captureActionsHandler;
    }

    public FollowFeedAdapter(Activity context, ArrayList<CaptureDetails> data,
            FeedItemActionsHandler actionsHandler,
            CaptureDetailsView.CaptureActionsHandler captureActionsHandler) {
        this(context, data, actionsHandler, captureActionsHandler, "");
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
        View view;
        switch (mCurrentState) {
            case DISPLAY_STATE_DETAILED:
                Log.d(TAG, "Displaying Detailed List Items");
                view = getDetailedListingView(position, convertView);
                break;
            case DISPLAY_STATE_SIMPLE:
            default:
                Log.d(TAG, "Displaying Simple List Items");
                view = getSimpleListingView(position, convertView);
                break;
        }

        if (position == mData.size() - 1 && mActionsHandler != null) {
            mActionsHandler.shouldLoadNextPage();
        }

        return view;
    }

    private View getDetailedListingView(int position, View convertView) {
        CaptureDetailsView rowView = null;
        CaptureDetails capture = mData.get(position);

        if (convertView instanceof CaptureDetailsView) {
            rowView = (CaptureDetailsView) convertView;
        }

        if (rowView == null) {
            rowView = new CaptureDetailsView(mContext);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        rowView.updateData(capture);
        return rowView;
    }

    public View getSimpleListingView(int position, View convertView) {
        CaptureSimpleItemRow rowView = null;
        CaptureDetails capture = mData.get(position);

        if (convertView instanceof CaptureSimpleItemRow) {
            rowView = (CaptureSimpleItemRow) convertView;
        }

        if (rowView == null) {
            rowView = new CaptureSimpleItemRow(mContext);
        }
        rowView.updateData(capture, mUserId);
        return rowView;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public void setCurrentState(int currentState) {
        mCurrentState = currentState;
    }

    public static interface FeedItemActionsHandler {

        public void shouldLoadNextPage();
    }
}
