package com.mienaikoe.wifimesh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesse on 1/23/2015.
 */
public class TrainLineIconAdapter extends BaseAdapter {

    private List<TrainLineIcon> icons;

    public TrainLineIconAdapter(Context context, List<String> symbols, int size) {
        super();
        icons = new ArrayList<TrainLineIcon>(symbols.size());
        for (String symbol : symbols) {
            this.icons.add(new TrainLineIcon(context, symbol, size));
        }
    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int position) {
        return icons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return icons.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return icons.get(position);
    }
}

