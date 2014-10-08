package com.delectable.mobile.jobs.accounts;


import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.events.accounts.UpdatedFollowersEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountsFollowersRequest;
import com.delectable.mobile.model.api.accounts.AccountsFollowersResponse;
import com.path.android.jobqueue.Params;

public class FetchFollowersJob extends BaseJob {

    private static final String TAG = FetchFollowersJob.class.getSimpleName();

    private String mId;

    private String mBefore;

    private String mAfter;

    private String mETag;

    /**
     * @param id      The id of the Account that you want to fetch followers for.
     * @param listing The previous ListingResponse if paginating. Pass in {@code null} if making a
     *                fresh request.
     */
    public FetchFollowersJob(String id, BaseListingResponse<AccountMinimal> listing) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mId = id;
        if (listing != null) {
            mBefore = listing.getBoundariesToBefore();
            mAfter = listing.getBoundariesToAfter();
            mETag = listing.getETag();
        }
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/accounts/followers";

        AccountsFollowersRequest request = new AccountsFollowersRequest(mETag, mId, mBefore,
                mAfter);

        AccountsFollowersResponse response = getNetworkClient()
                .post(endpoint, request, AccountsFollowersResponse.class);

        BaseListingResponse<AccountMinimal> accountListing = response.getPayload();

        // note: Sometimes payload may be null
        // maybe user has no followers
        // maybe list is completely up to date and e_tag_match is true
        mEventBus.post(new UpdatedFollowersEvent(mId, accountListing));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mEventBus.post(new UpdatedFollowersEvent(getErrorMessage()));
    }

}
