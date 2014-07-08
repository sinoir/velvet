package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NavigationAdapter extends BaseAdapter {

    private ArrayList<NavItemObject> mObjects;

    private Context mContext;

    private LayoutInflater mInflater;

    public NavigationAdapter(ArrayList<NavItemObject> objects, Context context) {
        mObjects = objects;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView rowView = (TextView) convertView;
        String title = mObjects.get(position).getTitle();
        StateListDrawable drawable = mObjects.get(position).getIcon();

        if (rowView == null) {
            rowView = (TextView) mInflater.inflate(R.layout.row_nav_item, null);
        }

        rowView.setText(title);
        if (drawable != null) {
            rowView.setCompoundDrawables(drawable, null, null, null);
        }

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
