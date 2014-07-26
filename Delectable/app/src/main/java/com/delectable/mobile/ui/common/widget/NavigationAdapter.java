package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.ui.navigation.widget.ActivityFeedRow;
import com.delectable.mobile.ui.navigation.widget.NavTextHeaderRow;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter {

    public static final int VIEW_TYPE_NAVIGATION = 0;

    public static final int VIEW_TYPE_HEADER = 1;

    public static final int VIEW_TYPE_FEED_ITEM = 2;

    private static final String TAG = NavigationAdapter.class.getSimpleName();

    private static final int sNumberViewTypes = 3;

    private ArrayList<NavItemObject> mNavigationItems;

    private ArrayList<ActivityRecipient> mActivityFeedData;

    private Context mContext;

    private LayoutInflater mInflater;

    private String mActivityFeedTitle;


    public NavigationAdapter(Context context, ArrayList<NavItemObject> navItems,
            ArrayList<ActivityRecipient> feedData) {
        mNavigationItems = navItems;
        mActivityFeedData = feedData;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mActivityFeedTitle = context.getResources()
                .getString(R.string.navigation_recent_activity_header);
    }

    @Override
    public int getCount() {
        // the 1 is the extra title between nav items and feed data
        return mNavigationItems.size() + mActivityFeedData.size();
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        // Position offset for Activity Feed Listing
        int positionOffset = position - mNavigationItems.size();
        switch (getItemViewType(position)) {
            case VIEW_TYPE_NAVIGATION:
                obj = mNavigationItems.get(position);
                break;
            case VIEW_TYPE_HEADER:
                obj = mActivityFeedTitle;
                break;
            case VIEW_TYPE_FEED_ITEM:
                obj = mActivityFeedData.get(positionOffset);
                break;
        }
        return obj;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return sNumberViewTypes;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        if (position < mNavigationItems.size()) {
            type = VIEW_TYPE_NAVIGATION;
        } else if (position == mNavigationItems.size()) {
            type = VIEW_TYPE_HEADER;
        } else {
            type = VIEW_TYPE_FEED_ITEM;
        }
        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "GetView: " + position);
        View view;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_NAVIGATION:
                view = getNavigationView(position, convertView);
                break;
            case VIEW_TYPE_FEED_ITEM:
                view = getActivityFeedView(position, convertView);
                break;
            case VIEW_TYPE_HEADER:
            default:
                view = getHeaderView(position, convertView);
                break;
        }
        return view;
    }

    private View getNavigationView(int position, View convertView) {
        TextView rowView = (TextView) convertView;
        NavItemObject navItem = (NavItemObject) getItem(position);
        String title = navItem.getTitle();
        StateListDrawable drawable = navItem.getIcon();

        if (rowView == null) {
            rowView = (TextView) mInflater.inflate(R.layout.row_nav_item, null);
        }

        rowView.setText(title);
        if (drawable != null) {
            rowView.setCompoundDrawables(drawable, null, null, null);
        }
        return rowView;
    }

    private View getActivityFeedView(int position, View convertView) {
        ActivityFeedRow rowView = (ActivityFeedRow) convertView;
        ActivityRecipient data = (ActivityRecipient) getItem(position);
        if (rowView == null) {
            rowView = new ActivityFeedRow(mContext);
        }
        rowView.updateData(data);

        return rowView;
    }

    private View getHeaderView(int position, View convertView) {
        NavTextHeaderRow rowView = (NavTextHeaderRow) convertView;

        if (rowView == null) {
            rowView = new NavTextHeaderRow(mContext);
        }
        rowView.setTitle(mActivityFeedTitle);
        return rowView;
    }

    public static class NavItemObject {

        private String title;

        private StateListDrawable icon;

        public NavItemObject(String title, StateListDrawable icon) {
            this.title = title;
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public StateListDrawable getIcon() {
            return icon;
        }

        public void setIcon(StateListDrawable icon) {
            this.icon = icon;
        }
    }
}
