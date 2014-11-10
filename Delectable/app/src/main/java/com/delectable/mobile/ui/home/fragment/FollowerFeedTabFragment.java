package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.ui.common.widget.Delectabutton;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.navigation.widget.NavHeader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        View emptyView = view.findViewById(R.id.empty_view_following);
        Delectabutton followButton = (Delectabutton) emptyView
                .findViewById(R.id.search_friends_button);
        followButton.setIconDrawable(
                getResources().getDrawable(R.drawable.ic_nav_drawer_friends_normal));
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventBus.post(new NavigationEvent(NavHeader.NAV_FIND_FRIENDS));
            }
        });
        mListView.setEmptyView(emptyView);

        return view;
    }

    @Override
    protected Listing<CaptureDetails, String> getCachedFeed() {
        return mCaptureListingModel.getFollowerFeed();
    }

    @Override
    protected void fetchCaptures(Listing<CaptureDetails, String> listing,
            boolean isPullToRefresh) {
        mAccountController
                .fetchFollowerFeed(CAPTURES_REQ, CapturesContext.DETAILS, listing,
                        isPullToRefresh);
    }

    @Override
    public void onEventMainThread(UpdatedListingEvent<CaptureDetails, String> event) {
        Log.d(TAG, "UpdatedListingEvent");
        if (!CAPTURES_REQ.equals(event.getRequestId())) {
            return;
        }
        super.onEventMainThread(event);
    }
}
