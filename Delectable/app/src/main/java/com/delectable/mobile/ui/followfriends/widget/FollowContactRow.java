package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.ui.common.widget.FontTextView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FollowContactRow extends RelativeLayout {

    @InjectView(R.id.name)
    protected FontTextView mName;

    @InjectView(R.id.follow_button)
    protected ImageButton mFollowButton;

    private AccountMinimal mAccount;

    private FollowActionsHandler mActionsHandler;

    public FollowContactRow(Context context) {
        this(context, null);
    }

    public FollowContactRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowContactRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_find_contact, this);
        ButterKnife.inject(this);
    }

    public void setActionsHandler(FollowActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    private void updateData(String name, boolean isFollowing) {
        mName.setText(name);
        mFollowButton.setSelected(isFollowing);
    }

    public void updateData(AccountMinimal account) {
        mAccount = account;
        updateData(account.getFullName(), account.isUserRelationshipTypeFollowing());
    }

    @OnClick(R.id.follow_button)
    protected void onFollowButtonClick(ImageButton v) {
        v.setSelected(!v.isSelected());
        if (mActionsHandler != null) {
            mActionsHandler.toggleFollow(mAccount, v.isSelected());
        }
    }

    @OnClick(R.id.name)
    public void showUserProfile() {
        if (mActionsHandler != null && mAccount != null) {
            mActionsHandler.showUserProfile(mAccount.getId());
        }
    }
}
