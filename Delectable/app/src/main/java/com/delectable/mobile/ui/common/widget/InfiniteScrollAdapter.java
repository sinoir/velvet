package com.delectable.mobile.ui.common.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public abstract class InfiniteScrollAdapter<T> extends BaseAdapter {

    protected ArrayList<T> mItems = new ArrayList<T>();

    protected ActionsHandler mActionsHandler;

    public InfiniteScrollAdapter(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Subclasses should call super on this method in order to ensure that the infinite scroll
     * callback gets called.
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (position == mItems.size() - 1 && mActionsHandler != null) {
            mActionsHandler.shouldLoadNextPage();
        }
        return view;
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public ArrayList<T> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<T> items) {
        mItems = items;
    }

    public interface ActionsHandler {

        public void shouldLoadNextPage();
    }
}