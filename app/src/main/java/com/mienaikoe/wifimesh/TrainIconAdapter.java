package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainLine;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TrainIconAdapter extends RecyclerView.Adapter<TrainIconAdapter.ViewHolder> {

    private OnItemClickListener mClickListener;
    private int mSelectedPosition = -1;
    private ArrayList<TrainLine> mLines = new ArrayList<TrainLine>();


    public TrainIconAdapter(OnItemClickListener listener) {
        mClickListener = listener;
    }


    public void setItems(ArrayList<TrainLine> items) {
        mLines = items;
    }

    public ArrayList<TrainLine> getItems() {
        return mLines;
    }

    public TrainLine getItem(int position) {
        return mLines.get(position);
    }

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;
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
        viewHolder.bindData(getItem(position), position);
        viewHolder.mIcon.setSelected(mSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return mLines.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TrainLineIcon mIcon;
        TrainLine mLine;
        int mPosition;
        OnItemClickListener mListener;

        public ViewHolder(TrainLineIcon icon, OnItemClickListener listener) {
            super(icon);
            mIcon = icon;
            mIcon.setOnClickListener(this);
            mListener = listener;
        }

        public void bindData(TrainLine line, int position) {
            mPosition = position;
            mLine = line;
            mIcon.setTrainLine(line);
        }


        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, mLine, mPosition);
            }
        }
    }

    public interface OnItemClickListener {

        public void onItemClick(View view, TrainLine trainline, int position);

    }
}


