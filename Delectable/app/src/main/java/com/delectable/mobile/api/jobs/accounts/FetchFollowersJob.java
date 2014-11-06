package com.delectable.mobile.api.jobs.accounts;


import com.delectable.mobile.api.cache.FollowersFollowingModel;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.jobs.BaseFetchListingJob;
import com.delectable.mobile.api.models.Listing;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.AccountMinimal;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FetchFollowersJob extends BaseFetchListingJob<AccountMinimal> {

    private static final String TAG = FetchFollowersJob.class.getSimpleName();

    @Inject
    protected FollowersFollowingModel mListingModel;


    /**
     * @param requestId       for the implementing UI to validate the callback Event.
     * @param accountId       The id of the Account that you want to fetch followers for.
     * @param listing         The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh whether or not user pull to refresh. If so, the listing will only
     *                        return recent updates and not paginate the list for past items.
     */
    public FetchFollowersJob(String requestId, String accountId,
            Listing<AccountMinimal, String> listing,
            Boolean isPullToRefresh) {
        //context is not passed in bc this endpoint only returns account minimal contexts
        super(requestId, null, accountId, listing, isPullToRefresh);
    }

    @Override
    protected String getEndpoint() {
        return "/accounts/followers";
    }

    @Override
    protected Listing<AccountMinimal, String> getCachedListing(String accountId) {
        return mListingModel.getFollowersListing(accountId);
    }

    @Override
    protected void saveListingToCache(String accountId,
            Listing<AccountMinimal, String> listing) {
        mListingModel.saveCurrentFollowersListing(accountId, listing);
    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<ListingResponse<AccountMinimal>>() {
        }.getType();
        return type;
    }
}
