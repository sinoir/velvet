package com.delectable.mobile.jobs.accounts;


import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.events.accounts.UpdatedFollowingsEvent;

public class FetchFollowingsJob extends BaseFetchFollowersFollowingsJob {

    private static final String TAG = FetchFollowersJob.class.getSimpleName();

    /**
     * @param id      The id of the Account that you want to fetch followers for.
     * @param listing The previous ListingResponse if paginating. Pass in {@code null} if making a
     *                fresh request.
     */
    public FetchFollowingsJob(String id, BaseListingResponse<AccountMinimal> listing) {
        super(id, listing);
    }

    @Override
    protected String getEndpoint() {
        return "/accounts/following";
    }

    @Override
    protected void postSuccessEvent(String id, BaseListingResponse<AccountMinimal> accountListing) {
        mEventBus.post(new UpdatedFollowingsEvent(id, accountListing));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mEventBus.post(new UpdatedFollowingsEvent(getErrorMessage()));
    }
}
