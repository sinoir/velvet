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

public class FollowTwitterFriendsRow extends RelativeLayout {

    @InjectView(R.id.name)
    protected FontTextView mName;

    @InjectView(R.id.subtext)
    protected FontTextView mInfluencerTitles;

    @InjectView(R.id.follow_button)
    protected ImageButton mFollowButton;

    private FollowActionsHandler mActionsHandler;

    private AccountMinimal mAccount;


    public FollowTwitterFriendsRow(Context context) {
        this(context, null);
    }

    public FollowTwitterFriendsRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowTwitterFriendsRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_find_twitter_friends, this);
        ButterKnife.inject(this);
    }

    public void setActionsHandler(FollowActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateData(AccountMinimal account) {
        mAccount = account;
        updateData(account.getFullName(),
                "@"+account.getTwScreenName(),
                account.isUserRelationshipTypeFollowing());
    }

    public void updateData(String name, String twitterScreenName,
            boolean isFollowing) {
        mName.setText(name);
        mInfluencerTitles.setText(twitterScreenName);
        mFollowButton.setSelected(isFollowing);
    }

    @OnClick(R.id.follow_button)
    protected void onFollowButtonClick(ImageButton v) {
        v.setSelected(!v.isSelected());
        if (mActionsHandler != null) {
            mActionsHandler.toggleFollow(mAccount, v.isSelected());
        }
    }
}
