package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.api.models.AccountMinimal;

import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * All of the follow friends fragment's rows are based on the same data, an array of AccountMinimal.
 * This handles that data, but leaves the getView method for subclasses to implement.
 */
public abstract class BaseAccountsMinimalAdapter extends BaseAdapter {

    protected ArrayList<AccountMinimal> mAccounts = new ArrayList<AccountMinimal>();

    protected FollowActionsHandler mActionsHandler;

    protected BaseAccountsMinimalAdapter(FollowActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void setAccounts(ArrayList<AccountMinimal> accounts) {
        mAccounts = accounts;
    }

    @Override
    public int getCount() {
        return mAccounts.size();
    }

    @Override
    public AccountMinimal getItem(int position) {
        return mAccounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
