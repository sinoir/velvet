package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.ui.common.widget.CircleImageView;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FollowExpertsRow extends RelativeLayout {

    @InjectView(R.id.profile_image)
    protected CircleImageView mProfileImage;

    @InjectView(R.id.name)
    protected FontTextView mName;

    @InjectView(R.id.influencer_titles)
    protected FontTextView mInfluencerTitles;

    @InjectView(R.id.follow_button)
    protected ImageButton mFollowButton;

    private AccountMinimal mAccount;

    private ActionsHandler mActionsHandler;

    public FollowExpertsRow(Context context) {
        this(context, null);
    }

    public FollowExpertsRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FollowExpertsRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_find_experts, this);
        ButterKnife.inject(this);
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    private void updateData(String profileImageUrl, String name, String influencerTitles,
            boolean isFollowing) {
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mProfileImage);
        mName.setText(name);
        mInfluencerTitles.setText(influencerTitles);
        mFollowButton.setSelected(isFollowing);
    }

    public void updateData(AccountMinimal account) {
        mAccount = account;
        updateData(account.getPhoto().getUrl(),
                account.getFullName(),
                account.getInfluencerTitlesString(),
                false);
    }

    @OnClick(R.id.follow_button)
    protected void onFollowButtonClick(ImageButton v) {
        v.setSelected(!v.isSelected());
        if (mActionsHandler != null) {
            mActionsHandler.toggleFollow(mAccount, v.isSelected());
        }
    }


    public static interface ActionsHandler {

        public void toggleFollow(AccountMinimal account, boolean isFollowing);
    }

}
