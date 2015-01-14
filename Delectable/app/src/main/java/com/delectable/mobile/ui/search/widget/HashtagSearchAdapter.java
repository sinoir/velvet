package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.HashtagResult;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HashtagSearchAdapter extends InfiniteScrollAdapter<SearchHit<HashtagResult>> {

    public HashtagSearchAdapter(ActionsHandler infiniteScrollHandler) {
        super(infiniteScrollHandler);
    }

    @Override
    public HashtagResult getItem(int position) {
        return mItems.get(position).getObject();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        SearchHashtagRow row = (SearchHashtagRow) view;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (SearchHashtagRow) inflater
                    .inflate(R.layout.row_search_hashtag_impl, parent, false);
        }
        row.updateData(getItem(position));

        return row;
    }
}
