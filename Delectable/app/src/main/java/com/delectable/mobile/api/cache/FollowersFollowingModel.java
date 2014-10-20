package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.cache.localmodels.CacheListing;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.iainconnor.objectcache.CacheManager;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

public class FollowersFollowingModel {

    private static final String TAG = FollowersFollowingModel.class.getSimpleName();

    private static final String TYPE_FOLLOWERS = TAG + "_followers_";

    private static final String TYPE_FOLLOWING = TAG + "_following_";

    @Inject
    protected CacheManager mCache;

    @Inject
    protected AccountModel mAccountModel;

    public void saveCurrentFollowersListing(String accountId,
            BaseListingResponse<AccountMinimal> listing) {
        String key = TYPE_FOLLOWERS + accountId;
        saveListing(key, listing);
    }

    public void saveCurrentFollowingListing(String accountId,
            BaseListingResponse<AccountMinimal> listing) {
        String key = TYPE_FOLLOWING + accountId;
        saveListing(key, listing);
    }

    public BaseListingResponse<AccountMinimal> getFollowersListing(String accountId) {
        String key = TYPE_FOLLOWERS + accountId;
        return getCachedAccounts(key);
    }

    public BaseListingResponse<AccountMinimal> getFollowingListing(String accountId) {
        String key = TYPE_FOLLOWING + accountId;
        return getCachedAccounts(key);
    }

    private BaseListingResponse<AccountMinimal> getListing(String key) {
        Type classType = new TypeToken<BaseListingResponse<AccountMinimal>>() {
        }.getType();
        BaseListingResponse<AccountMinimal> cachelisting
                = (BaseListingResponse<AccountMinimal>) mCache
                .get(key, null, classType);
        return cachelisting;
    }

    private void saveListing(String key, BaseListingResponse<AccountMinimal> listing) {

        CacheListing<AccountMinimal> cacheListing = new CacheListing<AccountMinimal>(listing);
        mCache.put(key, cacheListing);
        // Save all captures separately
        for (AccountMinimal account : listing.getUpdates()) {
            mAccountModel.saveAccountMinimal(account);
        }
    }

    private BaseListingResponse<AccountMinimal> getCachedAccounts(String key) {
        Type classType = new TypeToken<CacheListing<AccountMinimal>>() {
        }.getType();
        CacheListing<AccountMinimal> cachelisting = (CacheListing<AccountMinimal>) mCache.get(key, null, classType);
        if (cachelisting == null) {
            //nothing in cache
            return null;
        }

        //build ListingResponse from CacheListing
        ArrayList<AccountMinimal> accounts = new ArrayList<AccountMinimal>();
        for (String captureId : cachelisting.getItemIds()) {
            AccountMinimal account = mAccountModel.getAccountMinimal(captureId);
            if (account != null) {
                accounts.add(account);
            } else {
                Log.e(TAG, "Listing from cache inconsistency, capture id from cachelisting object not found in cache");
            }
        }
        BaseListingResponse<AccountMinimal> listing = new BaseListingResponse<AccountMinimal>(
                cachelisting, accounts);

        return listing;
    }


}
