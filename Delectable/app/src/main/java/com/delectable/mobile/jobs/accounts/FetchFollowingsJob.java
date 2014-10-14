package com.delectable.mobile.jobs.accounts;


import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.data.FollowersFollowingModel;
import com.delectable.mobile.jobs.BaseFetchListingJob;
import com.delectable.mobile.model.api.BaseListingWrapperResponse;

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
            BaseListingResponse<AccountMinimal> listing,
            Boolean isPullToRefresh) {
        //context is not passed in bc this endpoint only returns account minimal contexts
        super(requestId, null, accountId, listing, isPullToRefresh);
    }

    @Override
    protected String getEndpoint() {
        return "/accounts/following";
    }

    @Override
    protected BaseListingResponse<AccountMinimal> getCachedListing(String accountId) {
        return mListingModel.getFollowingListing(accountId);
    }

    @Override
    protected void saveListingToCache(String accountId,
            BaseListingResponse<AccountMinimal> listing) {
        mListingModel.saveCurrentFollowingListing(accountId, listing);

    }

    @Override
    public Type getResponseType() {
        Type type = new TypeToken<BaseListingWrapperResponse<AccountMinimal>>() {
        }.getType();
        return type;
    }
}
