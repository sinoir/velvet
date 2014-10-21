package com.delectable.mobile.api.jobs.accounts;


import com.delectable.mobile.api.cache.FollowersFollowingModel;
import com.delectable.mobile.api.endpointmodels.ListingResponse;
import com.delectable.mobile.api.jobs.BaseFetchListingJob;
import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.Listing;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class FetchFollowingsJob extends BaseFetchListingJob<AccountMinimal> {

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
    public FetchFollowingsJob(String requestId, String accountId,
            Listing<AccountMinimal> listing,
            Boolean isPullToRefresh) {
        //context is not passed in bc this endpoint only returns account minimal contexts
        super(requestId, null, accountId, listing, isPullToRefresh);
    }

    @Override
    protected String getEndpoint() {
        return "/accounts/following";
    }

    @Override
    protected Listing<AccountMinimal> getCachedListing(String accountId) {
        return mListingModel.getFollowingListing(accountId);
    }

    @Override
    protected void saveListingToCache(String accountId,
            Listing<AccountMinimal> listing) {
        mListingModel.saveCurrentFollowingListing(accountId, listing);

    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<ListingResponse<AccountMinimal>>() {
        }.getType();
        return type;
    }
}
