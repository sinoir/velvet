package com.delectable.mobile.jobs.accounts;


import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.events.accounts.UpdatedFollowersEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountsFollowersRequest;
import com.delectable.mobile.model.api.accounts.AccountsFollowersResponse;
import com.path.android.jobqueue.Params;

import javax.inject.Inject;

public class FetchFollowersJob extends BaseJob {

    private static final String TAG = FetchFollowersJob.class.getSimpleName();

    private String mId;

    private Float mBefore;

    private Float mAfter;

    public FetchFollowersJob(String id, Float before, Float after) {
        super(new Params(Priority.UX).requireNetwork().persist());
        mId = id;
        mBefore = before;
        mAfter = after;
    }

    @Override
    public void onAdded() {
        super.onAdded();
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/accounts/followers";

        AccountsFollowersRequest request = new AccountsFollowersRequest(mId, mBefore, mAfter);

        AccountsFollowersResponse response = getNetworkClient()
                .post(endpoint, request, AccountsFollowersResponse.class);

        BaseListingResponse<AccountMinimal> accountListing = response.getPayload();

        // Sometimes Payload may be null, maybe user has no followers
        if (accountListing != null) {

            getEventBus().post(new UpdatedFollowersEvent(mId, accountListing.getUpdates()));
        }
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mEventBus.post(new UpdatedFollowersEvent(getErrorMessage()));
    }

}
