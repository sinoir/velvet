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

    public static final int VIEW_TYPE_SIMPLE = 0;

    public static final int VIEW_TYPE_DETAILED = 1;

    private int mCurrentViewType = VIEW_TYPE_DETAILED;

    private static final String TAG = FollowFeedAdapter.class.getSimpleName();

    private static final int sNumberViewTypes = 2;


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
        mUserId = userId;
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
    public int getViewTypeCount() {
        return sNumberViewTypes;
    }

    @Override
    public int getItemViewType(int position) {
        return mCurrentViewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        switch (mCurrentViewType) {
            case VIEW_TYPE_DETAILED:
                Log.d(TAG, "Displaying Detailed List Items");
                view = getDetailedListingView(position, convertView);
                break;
            case VIEW_TYPE_SIMPLE:
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

        rowView = (CaptureDetailsView) convertView;

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

        rowView = (CaptureSimpleItemRow) convertView;

        if (rowView == null) {
            rowView = new CaptureSimpleItemRow(mContext);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        rowView.updateData(capture, mUserId);
        return rowView;
    }

    public int getCurrentViewType() {
        return mCurrentViewType;
    }

    public void setCurrentViewType(int currentViewType) {
        mCurrentViewType = currentViewType;
    }

    public static interface FeedItemActionsHandler {

        public void shouldLoadNextPage();
    }
}
