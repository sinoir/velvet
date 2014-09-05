package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.api.models.SearchHit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class AccountSearchAdapter extends BaseAdapter {

    private ArrayList<SearchHit<AccountSearch>> mHits = new ArrayList<SearchHit<AccountSearch>>();

    private SearchPeopleRow.ActionsHandler mActionsHandler;

    public AccountSearchAdapter(SearchPeopleRow.ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setHits(ArrayList<SearchHit<AccountSearch>> hits) {
        mHits = hits;
    }


    @Override
    public int getCount() {
        return mHits.size();
    }

    @Override
    public AccountSearch getItem(int position) {
        return mHits.get(position).getObject();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchPeopleRow row = (SearchPeopleRow) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (SearchPeopleRow) inflater
                    .inflate(R.layout.row_search_people_impl, parent, false);
            row.setActionsHandler(mActionsHandler);
        }
        row.updateData(getItem(position));

        return row;
    }
}
