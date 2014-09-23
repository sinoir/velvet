package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.Capture;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.profile.widget.CaptureSimpleItemRow;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class FollowFeedAdapter extends BaseAdapter {

    public static final int VIEW_TYPE_SIMPLE = 0;

    public static final int VIEW_TYPE_DETAILED = 1;

    private int mCurrentViewType = VIEW_TYPE_DETAILED;

    // Show Empty Row when there's no data to enable Scrolling of the ListView
    public static final int VIEW_TYPE_EMPTY = 2;

    private static final String TAG = FollowFeedAdapter.class.getSimpleName();

    private static final int sNumberViewTypes = 3;

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
        // Show Empty Row when there's no data to enable Scrolling of the ListView
        return mData.size() == 0 ? 1 : mData.size();
    }

    @Override
    public Capture getItem(int position) {
        // Show Empty Row when there's no data to enable Scrolling of the ListView
        return mData.size() == 0 ? null : mData.get(position);
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
        if (mData.size() == 0) {
            return VIEW_TYPE_EMPTY;
        }
        return mCurrentViewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_EMPTY:
                Log.d(TAG, "Displaying Empty View Items");
                view = getEmptyView(position, convertView, parent);
                break;
            case VIEW_TYPE_DETAILED:
                Log.d(TAG, "Displaying Detailed List Items");
                view = getDetailedListingView(position, convertView, parent);
                break;
            case VIEW_TYPE_SIMPLE:
            default:
                Log.d(TAG, "Displaying Simple List Items");
                view = getSimpleListingView(position, convertView, parent);
                break;
        }

        if (position == mData.size() - 1 && mActionsHandler != null) {
            mActionsHandler.shouldLoadNextPage();
        }

        return view;
    }

    /**
     * Shows an Empty Row when there's no data to enable Scrolling of the ListView
     */
    private View getEmptyView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rowView = (RelativeLayout) convertView;
        if (rowView == null) {
            // TODO: Can pop in empty state here.
            // Note: Height of this must be same as parent to allow scrolling for empty views, used in UserProfile
            rowView = new RelativeLayout(parent.getContext());
            rowView.setMinimumHeight(parent.getHeight());
        }
        return rowView;
    }

    private View getDetailedListingView(int position, View convertView, ViewGroup parent) {
        CaptureDetailsView rowView = null;
        CaptureDetails capture = mData.get(position);

        rowView = (CaptureDetailsView) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (CaptureDetailsView) inflater.inflate(R.layout.row_feed_wine_detail_impl,
                    parent, false);
            rowView.setActionsHandler(mCaptureActionsHandler);
        }
        rowView.updateData(capture);
        return rowView;
    }

    public View getSimpleListingView(int position, View convertView, ViewGroup parent) {
        CaptureSimpleItemRow rowView = null;
        CaptureDetails capture = mData.get(position);

        rowView = (CaptureSimpleItemRow) convertView;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (CaptureSimpleItemRow) inflater.inflate(R.layout.row_simple_wine_detail_impl,
                    parent, false);
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
