package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.CaptureListingModel;
import com.delectable.mobile.events.UpdatedListingEvent;
import com.delectable.mobile.model.api.accounts.CapturesContext;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

public class FollowerFeedTabFragment extends BaseCaptureFeedFragment implements
        InfiniteScrollAdapter.ActionsHandler {

    private static final String TAG = FollowerFeedTabFragment.class.getSimpleName();

    private static final String CAPTURES_REQ = TAG + "_captures_req";

    @Inject
    protected CaptureListingModel mCaptureListingModel;

    @Inject
    protected AccountController mAccountController;

    public FollowerFeedTabFragment() {
        // Required empty public constructor
    }

    /**
     * @param accountId The account id is really just to initialize the adapter, because the rows
     *                  need to know whether the post is by the user or not.
     */
    public static FollowerFeedTabFragment newInstance(String accountId) {
        FollowerFeedTabFragment fragment = new FollowerFeedTabFragment();
        Bundle args = bundleArgs(accountId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseListingResponse<CaptureDetails> getCachedFeed() {
        return mCaptureListingModel.getFollowerFeed();
    }

    @Override
    protected void fetchCaptures(BaseListingResponse<CaptureDetails> listing,
            boolean isPullToRefresh) {
        mAccountController
                .fetchFollowerFeed(CAPTURES_REQ, CapturesContext.DETAILS, listing,
                        isPullToRefresh);
    }

    @Override
    public void onEventMainThread(UpdatedListingEvent<CaptureDetails> event) {
        Log.d(TAG, "UpdatedListingEvent");
        if (!CAPTURES_REQ.equals(event.getRequestId())) {
            return;
        }
        super.onEventMainThread(event);
    }
}
