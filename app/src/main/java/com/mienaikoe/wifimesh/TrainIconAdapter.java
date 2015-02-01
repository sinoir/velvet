package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainLine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TrainIconAdapter extends RecyclerView.Adapter<TrainIconAdapter.ViewHolder> {

    List<TrainLine> mLines = new ArrayList<TrainLine>();

    public void setItems(List<TrainLine> items) {
        mLines = items;
    }

    public List<TrainLine> getItems() {
        return mLines;
    }

    public TrainLine getItem(int position) {
        return mLines.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TrainLineIcon view = (TrainLineIcon) inflater
                .inflate(R.layout.train_line_icon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindData(getItem(position));
    }

    @Override
    public int getItemCount() {
        return mLines.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        TrainLineIcon mIcon;

        public ViewHolder(TrainLineIcon icon) {
            super(icon);
            mIcon = icon;
        }

        public void bindData(TrainLine line) {
            mIcon.setTrainLine(line);
        }
    }
}


