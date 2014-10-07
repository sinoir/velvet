package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * All of the follow friends fragment's rows are based on the same data, an array of AccountMinimal.
 * This handles that data, but leaves the getView method for subclasses to implement.
 */
public abstract class BaseAccountsMinimalAdapter extends BaseAdapter {

    protected static final int TYPE_HEADER = 0;

    protected static final int TYPE_ACCOUNT = 1;

    protected ArrayList<AccountMinimal> mAccounts = new ArrayList<AccountMinimal>();

    protected FollowActionsHandler mActionsHandler;

    private Integer mTopHeaderTitleResId;

    protected BaseAccountsMinimalAdapter(FollowActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setAccounts(ArrayList<AccountMinimal> accounts) {
        mAccounts = accounts;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mTopHeaderTitleResId != null) {
            return TYPE_HEADER;
        }
        return TYPE_ACCOUNT;
    }

    @Override
    public int getViewTypeCount() {
        if (mTopHeaderTitleResId == null) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getCount() {
        if (mTopHeaderTitleResId == null) {
            return mAccounts.size();
        } else {
            return mAccounts.size() + 1;
        }
    }

    @Override
    public Object getItem(int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return mTopHeaderTitleResId;
        }
        if (mTopHeaderTitleResId == null) {
            return mAccounts.get(position);
        }
        return mAccounts.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Integer getTopHeaderTitleResId() {
        return mTopHeaderTitleResId;
    }

    public void setTopHeaderTitleResId(int headerRes) {
        mTopHeaderTitleResId = headerRes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_ACCOUNT:
                return getFollowRow(position, convertView, parent);
            case TYPE_HEADER:
                return getHeaderRow(position, convertView, parent);
        }

        return null;
    }

    protected abstract View getFollowRow(int position, View convertView, ViewGroup parent);

    //TODO perhaps move this to be the listview's header
    protected View getHeaderRow(int position, View convertView, ViewGroup parent) {
        TextView row = (TextView) convertView;
        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            row = (TextView) inflater
                    .inflate(R.layout.header_find_people, parent, false);
        }
        // We use Resource ID
        row.setText((Integer) getItem(position));
        row.setClickable(false);
        return row;
    }
}
