package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainLine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TrainIconAdapter extends RecyclerView.Adapter<TrainIconAdapter.ViewHolder> {

    private OnItemClickListener mClickListener;

    public TrainIconAdapter(OnItemClickListener listener) {
        mClickListener = listener;
    }

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
        return new ViewHolder(view, mClickListener);
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
        TrainLine mLine;

        public ViewHolder(TrainLineIcon icon, final OnItemClickListener listener) {
            super(icon);
            mIcon = icon;
            mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null) {
                        listener.onItemClick(v, mLine);
                    }
                }
            });
        }

        public void bindData(TrainLine line) {
            mLine = line;
            mIcon.setTrainLine(line);
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, TrainLine trainline);

    }
}


