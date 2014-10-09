package com.delectable.mobile.jobs.accounts;


import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.events.accounts.UpdatedFollowersEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountsFollowersRequest;
import com.delectable.mobile.model.api.accounts.AccountsFollowersResponse;
import com.path.android.jobqueue.Params;

public class FetchFollowersJob extends BaseFetchFollowersFollowingsJob {

    private static final String TAG = FetchFollowersJob.class.getSimpleName();

    /**
     * @param id      The id of the Account that you want to fetch followers for.
     * @param listing The previous ListingResponse if paginating. Pass in {@code null} if making a
     *                fresh request.
     */
    public FetchFollowersJob(String id, BaseListingResponse<AccountMinimal> listing) {
        super(id, listing);
    }

    @Override
    protected String getEndpoint() {
        return "/accounts/followers";
    }

    @Override
    protected void postSuccessEvent(String id, BaseListingResponse<AccountMinimal> accountListing) {
        mEventBus.post(new UpdatedFollowersEvent(id, accountListing));

    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mEventBus.post(new UpdatedFollowersEvent(getErrorMessage()));
    }
}
