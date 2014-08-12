package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.ui.navigation.widget.ActivityFeedRow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ActivityFeedAdapter extends BaseAdapter {

    private static final String TAG = ActivityFeedAdapter.class.getSimpleName();

    private ArrayList<ActivityRecipient> mActivityFeedData;

    private Context mContext;

    private LayoutInflater mInflater;

    private String mActivityFeedTitle;

    private ActivityFeedRow.ActivityActionsHandler mActionsHandler;

    public ActivityFeedAdapter(Context context, ArrayList<ActivityRecipient> feedData) {
        mActivityFeedData = feedData;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mActivityFeedTitle = context.getResources()
                .getString(R.string.navigation_recent_activity_header);
    }

    @Override
    public int getCount() {
        return mActivityFeedData.size();
    }

    @Override
    public ActivityRecipient getItem(int position) {
        return mActivityFeedData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityFeedRow rowView = (ActivityFeedRow) convertView;
        ActivityRecipient data = getItem(position);
        if (rowView == null) {
            rowView = new ActivityFeedRow(mContext);
        }
        rowView.updateData(data);
        rowView.setActionsHandler(mActionsHandler);
        return rowView;
    }

    public void setActionsHandler(ActivityFeedRow.ActivityActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }
}