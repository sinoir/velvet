package com.delectable.mobile.ui.search.widget;

import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.ui.common.widget.BaseFollowAccountRow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class SearchPeopleRow extends BaseFollowAccountRow {

    private AccountSearch mAccount;

    private ActionsHandler mActionsHandler;

    public SearchPeopleRow(Context context) {
        this(context, null);
    }

    public SearchPeopleRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchPeopleRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateData(AccountSearch account) {
        mAccount = account;
        updateData(account.getPhoto().getUrl(),
                account.getFullName(),
                account.getBio(),
                account.isUserRelationshipTypeFollowing());
    }

    @Override
    protected void onFollowButtonClick(ImageButton v) {
        super.onFollowButtonClick(v);
        if (mActionsHandler != null) {
            mActionsHandler.toggleFollow(mAccount, v.isSelected());
        }
    }

    public interface ActionsHandler {

        public void toggleFollow(AccountSearch account, boolean isFollowing);
    }
}
