package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.SearchHit;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AccountSearchAdapter extends InfiniteScrollAdapter<SearchHit<AccountSearch>> {

    private SearchPeopleRow.ActionsHandler mRowActionsHandler;

    public AccountSearchAdapter(InfiniteScrollAdapter.ActionsHandler infiniteScrollHandler,
            SearchPeopleRow.ActionsHandler rowActionsHandler) {
        super(infiniteScrollHandler);
        mRowActionsHandler = rowActionsHandler;
    }

    @Override
    public AccountSearch getItem(int position) {
        return mItems.get(position).getObject();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        SearchPeopleRow row = (SearchPeopleRow) view;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (SearchPeopleRow) inflater
                    .inflate(R.layout.row_search_people_impl, parent, false);
            row.setActionsHandler(mRowActionsHandler);
        }
        row.updateData(getItem(position));

        return row;
    }
}
