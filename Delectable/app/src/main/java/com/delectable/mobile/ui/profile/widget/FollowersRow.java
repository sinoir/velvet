package com.delectable.mobile.ui.profile.widget;

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

public class FollowersRow extends RelativeLayout {

    public interface ActionsHandler {

        public void toggleFollow(AccountMinimal account, boolean isFollowing);
    }

    @InjectView(R.id.profile_image)
    protected CircleImageView mProfileImage;

    @InjectView(R.id.name)
    protected FontTextView mName;

    @InjectView(R.id.follow_button)
    protected ImageButton mFollowButton;

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
        View.inflate(context, R.layout.row_followers, this);
        ButterKnife.inject(this);
    }

    public void setActionsHandler(ActionsHandler actionsHandler) {
        mActionsHandler = actionsHandler;
    }

    public void updateData(AccountMinimal account) {
        mAccount = account;
        updateData(account.getPhoto().getBestThumb(),
                account.getFullName(),
                account.getCurrentUserRelationship());
    }

    public void updateData(String profileImageUrl, String name, int relationship) {
        //set no_photo first, so that when the user flicks through the list, it doesn't show another account's picture
        ImageLoaderUtil.loadImageIntoView(getContext(), R.drawable.no_photo, mProfileImage);
        ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mProfileImage);
        mName.setText(name);

        //set state of follow button depending on relationship
        if (relationship == AccountMinimal.RELATION_TYPE_SELF) {
            mFollowButton.setVisibility(View.INVISIBLE);
        }
        if (relationship == AccountMinimal.RELATION_TYPE_NONE) {
            mFollowButton.setVisibility(View.VISIBLE);
            mFollowButton.setSelected(false);
        }
        if (relationship == AccountMinimal.RELATION_TYPE_FOLLOWING) {
            mFollowButton.setVisibility(View.VISIBLE);
            mFollowButton.setSelected(true);
        }
    }

    @OnClick(R.id.follow_button)
    protected void onFollowButtonClick(ImageButton v) {
        v.setSelected(!v.isSelected());
        if (mActionsHandler != null) {
            mActionsHandler.toggleFollow(mAccount, v.isSelected());
        }
    }
}
