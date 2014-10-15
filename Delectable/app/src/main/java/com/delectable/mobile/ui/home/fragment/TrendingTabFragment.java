package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.data.CaptureListingModel;
import com.delectable.mobile.events.UpdatedListingEvent;
import com.delectable.mobile.model.api.accounts.CapturesContext;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

public class TrendingTabFragment extends BaseCaptureFeedFragment implements
        InfiniteScrollAdapter.ActionsHandler {

    private static final String TAG = TrendingTabFragment.class.getSimpleName();

    private static final String CAPTURES_REQ = TAG + "_captures_req";

    @Inject
    protected CaptureListingModel mCaptureListingModel;

    public TrendingTabFragment() {
        // Required empty public constructor
    }

    /**
     * @param accountId The account id is really just to initialize the adapter, because the rows
     *                  need to know whether the post is by the user or not.
     */
    public static TrendingTabFragment newInstance(String accountId) {
        TrendingTabFragment fragment = new TrendingTabFragment();
        Bundle args = bundleArgs(accountId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        View emptyView = view.findViewById(R.id.empty_view_trending);
        mListView.setEmptyView(emptyView);

        return view;
    }

    @Override
    protected BaseListingResponse<CaptureDetails> getCachedFeed() {
        return mCaptureListingModel.getTrendingFeed();
    }

    @Override
    protected void fetchCaptures(BaseListingResponse<CaptureDetails> listing,
            boolean isPullToRefresh) {
        mCaptureController
                .fetchTrendingCaptures(CAPTURES_REQ, CapturesContext.DETAILS, listing,
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
