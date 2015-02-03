package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainStation;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

    public class SearchAdapter extends CursorAdapter {

        private List<TrainStation> items;

        //private TextView text;

        public SearchAdapter(Context context, Cursor cursor, List<TrainStation> items) {

            super(context, cursor, false);

            this.items = items;

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TypefaceTextView textview = (TypefaceTextView) view;
            textview.setText(items.get(cursor.getPosition()).getName());

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.row_search, parent, false);
            return view;

        }

    }
