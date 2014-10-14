package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.ui.common.widget.BaseFollowAccountRow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import butterknife.OnClick;

public class FollowExpertsRow extends BaseFollowAccountRow {


    private AccountMinimal mAccount;

    private FollowActionsHandler mActionsHandler;

    public FollowExpertsRow(Context context) {
        this(context, null);
    }

    public FollowExpertsRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowExpertsRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setActionsHandler(FollowActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateData(AccountMinimal account) {
        mAccount = account;
        updateData(account.getPhoto().getBestThumb(),
                account.getFullName(),
                account.getInfluencerTitlesString(),
                account.getCurrentUserRelationship());
    }

    @Override
    protected void onFollowButtonClick(ImageButton v) {
        super.onFollowButtonClick(v);
        if (mActionsHandler != null) {
            mActionsHandler.toggleFollow(mAccount, v.isSelected());
        }
    }

    @OnClick(R.id.profile_image)
    public void showUserProfile() {
        if (mActionsHandler != null) {
            mActionsHandler.showUserProfile(mAccount.getId());
        }
    }
}
