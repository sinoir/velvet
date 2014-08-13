package com.delectable.mobile.ui.settings.widget;

import com.delectable.mobile.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SetProfilePicRowAdapter extends BaseAdapter {

    private ArrayList<String> mItems;


    public SetProfilePicRowAdapter(ArrayList<String> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public String getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView row = (TextView) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (TextView) inflater.inflate(R.layout.row_dialog_set_profile_pic, parent, false);
        }
        row.setText(getItem(position));
        return row;
    }
}
