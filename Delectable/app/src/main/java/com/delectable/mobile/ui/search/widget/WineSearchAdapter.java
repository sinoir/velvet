package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class WineSearchAdapter extends InfiniteScrollAdapter<SearchHit<BaseWineMinimal>> {

    public WineSearchAdapter(InfiniteScrollAdapter.ActionsHandler actionsHandler) {
        super(actionsHandler);
    }

    @Override
    public BaseWineMinimal getItem(int position) {
        return mItems.get(position).getObject();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        SearchWineRow row = (SearchWineRow) view;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (SearchWineRow) inflater
                    .inflate(R.layout.row_search_wine_impl, parent, false);
        }
        row.updateData(getItem(position));

        return row;
    }
}
