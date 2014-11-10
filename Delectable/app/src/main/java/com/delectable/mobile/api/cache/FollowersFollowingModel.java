package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.cache.localmodels.CacheListing;
import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.Listing;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

public class FollowersFollowingModel {

    private static final String TAG = FollowersFollowingModel.class.getSimpleName();

    private static final String TYPE_FOLLOWERS = TAG + "_followers_";

    private static final String TYPE_FOLLOWING = TAG + "_following_";

    private static final HashMap<String, CacheListing<AccountMinimal>> mMap
            = new HashMap<String, CacheListing<AccountMinimal>>();

    @Inject
    protected AccountModel mAccountModel;

    public void saveCurrentFollowersListing(String accountId,
            Listing<AccountMinimal, String> listing) {
        String key = TYPE_FOLLOWERS + accountId;
        saveListing(key, listing);
    }

    public void saveCurrentFollowingListing(String accountId,
            Listing<AccountMinimal, String> listing) {
        String key = TYPE_FOLLOWING + accountId;
        saveListing(key, listing);
    }

    public Listing<AccountMinimal, String> getFollowersListing(String accountId) {
        String key = TYPE_FOLLOWERS + accountId;
        return getCachedAccounts(key);
    }

    public Listing<AccountMinimal, String> getFollowingListing(String accountId) {
        String key = TYPE_FOLLOWING + accountId;
        return getCachedAccounts(key);
    }

    public static void clear() {
        mMap.clear();
    }

    private void saveListing(String key, Listing<AccountMinimal, String> listing) {

        CacheListing<AccountMinimal> cacheListing = new CacheListing<AccountMinimal>(listing);
        mMap.put(key, cacheListing);
        // Save all captures separately
        for (AccountMinimal account : listing.getUpdates()) {
            mAccountModel.saveAccountMinimal(account);
        }
    }

    private Listing<AccountMinimal, String> getCachedAccounts(String key) {
        CacheListing<AccountMinimal> cachelisting = mMap.get(key);
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
                Log.e(TAG,
                        "Listing from cache inconsistency, account id from cachelisting object not found in cache");
            }
        }
        Listing<AccountMinimal, String> listing = new Listing<AccountMinimal, String>(cachelisting, accounts);

        return listing;
    }


}
