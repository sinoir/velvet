package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.AccountSearch;
import com.delectable.mobile.ui.common.widget.BaseFollowAccountRow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class FollowersRow extends BaseFollowAccountRow {

    private AccountMinimal mAccount;

    private ActionsHandler mActionsHandler;

    public FollowersRow(Context context) {
        this(context, null);
    }

    public FollowersRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowersRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateData(AccountMinimal account) {
        mAccount = account;
        updateData(account.getPhoto().getBestThumb(),
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

        public void toggleFollow(AccountMinimal account, boolean isFollowing);
    }
}
