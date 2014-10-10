package com.delectable.mobile.jobs.accounts;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.data.CaptureListingModel;
import com.delectable.mobile.events.accounts.UpdatedAccountCapturesEvent;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.Priority;
import com.delectable.mobile.model.api.accounts.AccountsCapturesRequest;
import com.delectable.mobile.model.api.captures.CaptureFeedResponse;
import com.path.android.jobqueue.Params;

import java.util.ArrayList;

import javax.inject.Inject;

public class FetchAccountCapturesJob extends BaseJob {

    private static final String TAG = FetchAccountCapturesJob.class.getSimpleName();

    private AccountsCapturesRequest.Context mContext;

    private String mAccountId;

    private String mBefore;

    private String mAfter;

    private String mETag;

    private Boolean mIsPullToRefresh;

    @Inject
    protected CaptureListingModel mListingModel;

    /**
     * @param accountId       Account that you want to fetch captures for.
     * @param captureListing  The previous ListingResponse if paginating. Pass in {@code null} if
     *                        making a fresh request.
     * @param isPullToRefresh true if user invoke this call via a pull to refresh.
     */
    public FetchAccountCapturesJob(AccountsCapturesRequest.Context context, String accountId,
            BaseListingResponse<CaptureDetails> captureListing,
            Boolean isPullToRefresh) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mContext = context;
        mAccountId = accountId;
        if (captureListing != null) {
            mBefore = captureListing.getBoundariesToBefore();
            mAfter = captureListing.getBoundariesToAfter();
            mETag = captureListing.getETag();
        }
        mIsPullToRefresh = isPullToRefresh;
    }

    public FetchAccountCapturesJob(AccountsCapturesRequest.Context context, String accountId,
            String etag, String before,
            String after, Boolean isPullToRefresh) {
        super(new Params(Priority.SYNC).requireNetwork().persist());
        mContext = context;
        mAccountId = accountId;
        mETag = etag;
        mBefore = before;
        mAfter = after;
        mIsPullToRefresh = isPullToRefresh;
    }

    @Override
    public void onRun() throws Throwable {
        super.onRun();
        String endpoint = "/accounts/captures";

        AccountsCapturesRequest request = new AccountsCapturesRequest(mContext, mETag, mAccountId,
                mBefore, mAfter, mIsPullToRefresh);

        CaptureFeedResponse response = getNetworkClient().post(endpoint, request,
                CaptureFeedResponse.class);

        ListingResponse<CaptureDetails> apiListing = response.getPayload();
        // note: Sometimes payload may be null
        // maybe there are no captures
        // maybe list is completely up to date and e_tag_match is true

        BaseListingResponse<CaptureDetails> cachedListing = null;

        //update cache if the listing exists
        if (apiListing != null) {

            //grab cached listing if it exist
            cachedListing = mListingModel.getListing(mAccountId);
            ArrayList<CaptureDetails> cachedCaptures = new ArrayList<CaptureDetails>();
            if (cachedListing != null) {
                cachedCaptures = cachedListing.getUpdates();
            }

            //combine listing into current capturesList, and then set that List as as the updates array
            apiListing.combineInto(cachedCaptures, response.isInvalidate());
            apiListing.clearLists();
            apiListing.setUpdates(cachedCaptures);

            cachedListing = mListingModel.saveCurrentListing(mAccountId, apiListing);
        }

        mEventBus.post(new UpdatedAccountCapturesEvent(mAccountId, cachedListing));
    }

    @Override
    protected void onCancel() {
        super.onCancel();
        mEventBus.post(new UpdatedAccountCapturesEvent(getErrorMessage()));
    }
}
