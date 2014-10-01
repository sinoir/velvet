package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.ui.navigation.widget.ActivityFeedRow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ActivityFeedAdapter extends BaseAdapter {

    private static final String TAG = ActivityFeedAdapter.class.getSimpleName();

    private ArrayList<ActivityRecipient> mActivityFeedData = new ArrayList<ActivityRecipient>();

    private ActivityFeedRow.ActivityActionsHandler mActionsHandler;

    public ActivityFeedAdapter(ActivityFeedRow.ActivityActionsHandler handler) {
        mActionsHandler = handler;
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
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = (ActivityFeedRow) inflater.inflate(R.layout.row_activity_feed_impl, parent,
                    false);
        }
        rowView.updateData(data);
        rowView.setActionsHandler(mActionsHandler);
        return rowView;
    }

    public void setActionsHandler(ActivityFeedRow.ActivityActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setItems(ArrayList<ActivityRecipient> items) {
        mActivityFeedData = items;
    }
}
