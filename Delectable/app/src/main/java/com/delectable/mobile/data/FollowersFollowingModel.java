package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FollowersFollowingModel {

    private static final String TAG = FollowersFollowingModel.class.getSimpleName();

    private static final String TYPE_FOLLOWERS = TAG + "_followers_";

    private static final String TYPE_FOLLOWING = TAG + "_following_";

    @Inject
    protected CacheManager mCache;

    public boolean saveCurrentFollowersListing(String accountId,
            BaseListingResponse<AccountMinimal> listing) {
        return mCache.put(TYPE_FOLLOWERS + accountId, listing);
    }

    public boolean saveCurrentFollowingListing(String accountId,
            BaseListingResponse<AccountMinimal> listing) {
        return mCache.put(TYPE_FOLLOWING + accountId, listing);
    }

    public BaseListingResponse<AccountMinimal> getFollowersListing(String accountId) {
        String key = TYPE_FOLLOWERS + accountId;
        return getListing(key);
    }

    public BaseListingResponse<AccountMinimal> getFollowingListing(String accountId) {
        String key = TYPE_FOLLOWING + accountId;
        return getListing(key);
    }

    private BaseListingResponse<AccountMinimal> getListing(String key) {
        Type classType = new TypeToken<BaseListingResponse<AccountMinimal>>() {
        }.getType();
        BaseListingResponse<AccountMinimal> cachelisting
                = (BaseListingResponse<AccountMinimal>) mCache
                .get(key, null, classType);
        return cachelisting;
    }

    private class AccountMinimalListingResponse extends BaseListingResponse<AccountMinimal> {

    }


}
