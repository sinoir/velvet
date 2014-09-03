package com.delectable.mobile.ui.followfriends.widget;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.TaggeeContact;

public interface FollowActionsHandler {

    public void toggleFollow(AccountMinimal account, boolean isFollowing);

    public void inviteContact(TaggeeContact contact);
}
