package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.api.models.AccountMinimal;

public interface FollowActionsHandler {

    public void toggleFollow(AccountMinimal account, boolean isFollowing);
}
