package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.SearchHit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class WineSearchAdapter extends BaseAdapter {

    private ArrayList<SearchHit<BaseWine>> mHits = new ArrayList<SearchHit<BaseWine>>();

    public void setHits(ArrayList<SearchHit<BaseWine>> hits) {
        mHits = hits;
    }

    @Override
    public int getCount() {
        return mHits.size();
    }

    @Override
    public BaseWine getItem(int position) {
        return mHits.get(position).getObject();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchWineRow row = (SearchWineRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (SearchWineRow) inflater
                    .inflate(R.layout.row_search_wine_impl, parent, false);
        }
        row.updateData(getItem(position));

        return row;
    }
}
