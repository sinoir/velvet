package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.events.NavigationEvent;
import com.delectable.mobile.events.captures.UpdatedFollowerFeedEvent;
import com.delectable.mobile.events.captures.UpdatedUserCaptureFeedEvent;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.FollowFeedAdapter;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.util.SafeAsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import javax.inject.Inject;

public class FollowFeedTabFragment extends BaseCaptureDetailsFragment implements
        FollowFeedAdapter.FeedItemActionsHandler {

    private static final String TAG = FollowFeedTabFragment.class.getSimpleName();

    private static final String sArgsUserId = "sArgsUserId";

    @Inject
    CaptureDetailsListingModel mCaptureListingModel;

    @Inject
    CaptureController mCaptureController;

    private View mView;

    private SwipeRefreshLayout mRefreshContainer;

    private ListView mListView;

    private FollowFeedAdapter mAdapter;

    private ListingResponse<CaptureDetails> mDetailsListing;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private boolean mIsLoadingData;

    private String mUserId;

    public FollowFeedTabFragment() {
        // Required empty public constructor
    }

    // FIXME Possibly split this up in 2 fragment classes, 1 for User's captures, and 1 for Following.
    public static FollowFeedTabFragment newInstance() {
        return newInstance(null);
    }

    public static FollowFeedTabFragment newInstance(String userId) {
        FollowFeedTabFragment fragment = new FollowFeedTabFragment();
        Bundle args = new Bundle();
        args.putString(sArgsUserId, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        mCaptureDetails = new ArrayList<CaptureDetails>();
        mIsLoadingData = false;
        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getString(sArgsUserId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater
                .inflate(R.layout.fragment_home_follow_feed_tab, container,
                        false);
        mRefreshContainer = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);

        mListView = (ListView) mView.findViewById(R.id.list_view);

        if (mUserId == null) {
            // FOLLOWING Tab
            View emptyView = mView.findViewById(R.id.empty_view_following);
            View followButton = emptyView.findViewById(R.id.search_friends_button);
            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEventBus.post(new NavigationEvent(NavHeader.NAV_FIND_FRIENDS));
                }
            });
            mListView.setEmptyView(emptyView);
        } else {
            // YOU Tab
            // TODO
            //View emptyView = mView.findViewById(R.id.empty_view_you);
            //mListView.setEmptyView(emptyView);
        }

        mAdapter = new FollowFeedAdapter(getActivity(), mCaptureDetails, this, this);
        mListView.setAdapter(mAdapter);

        setupPullToRefresh();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocalData();
        refreshData();
    }

    private void setupPullToRefresh() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view,
                    int firstVisibleItem,
                    int visibleItemCount,
                    int totalItemCount) {
                int topRowVerticalPosition = (mListView == null || mListView.getChildCount() == 0)
                        ?
                        0 : mListView.getChildAt(0).getTop();
                mRefreshContainer.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        mRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "On Refresh");
                refreshData();
            }
        });
    }

    private void refreshData() {
        loadRemoteData(true);
    }

    private void loadNextPage() {
        // TODO: Pop this logic somewhere else, like "hasNextPage" or something...
        Log.d(TAG, "Load Next Page? " + mDetailsListing.getMore());
        // TODO: Figure out why last page returns invalidate and doesn't load more pages, why is more true?
        if (mDetailsListing != null && mDetailsListing.getMore()
                && mDetailsListing.getBoundariesFromAfter() == null) {
            loadRemoteData(false);
        }
    }

    public void onEventMainThread(UpdatedFollowerFeedEvent event) {
        //FIXME : Will Split this class in 2.
        if (mUserId != null) {
            return;
        }
        mRefreshContainer.setRefreshing(false);
        mIsLoadingData = false;
        if (event.isSuccessful()) {
            loadLocalData();
        } else if (event.getErrorMessage() != null) {
            showToastError(event.getErrorMessage());
        }
    }

    public void onEventMainThread(UpdatedUserCaptureFeedEvent event) {
        //FIXME : Will Split this class in 2.
        if (mUserId == null || !mUserId.equals(event.getAccountId())) {
            return;
        }
        mRefreshContainer.setRefreshing(false);
        mIsLoadingData = false;
        if (event.isSuccessful()) {
            loadLocalData();
        } else if (event.getErrorMessage() != null) {
            showToastError(event.getErrorMessage());
        }
    }

    private void loadLocalData() {
        if (mUserId != null) {
            loadUserCaptureFeed();
        } else {
            loadFollowFeed();
        }
    }

    private void loadFollowFeed() {
        new SafeAsyncTask<ListingResponse<CaptureDetails>>(this) {
            @Override
            protected ListingResponse<CaptureDetails> safeDoInBackground(Void[] params) {
                Log.d(TAG, "Loading Local Follower Captures");
                return mCaptureListingModel.getFollowerFeedCaptures();
            }

            @Override
            protected void safeOnPostExecute(ListingResponse<CaptureDetails> data) {
                Log.d(TAG, "Post Loading Local Follower Captures: " + data);
                updateLocalData(data);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadUserCaptureFeed() {
        new SafeAsyncTask<ListingResponse<CaptureDetails>>(this) {
            @Override
            protected ListingResponse<CaptureDetails> safeDoInBackground(Void[] params) {
                return mCaptureListingModel.getUserCaptures(mUserId);
            }

            @Override
            protected void safeOnPostExecute(ListingResponse<CaptureDetails> data) {
                updateLocalData(data);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateLocalData(ListingResponse<CaptureDetails> data) {
        mDetailsListing = data;
        updateDisplayData();
    }

    private void loadRemoteData(boolean isRefreshing) {
        Log.d(TAG, "Load Data isRefreshing: ? " + isRefreshing);
        if (!mIsLoadingData) {
            // TODO: Split fragments with base ..
            if (mUserId != null) {
                if (isRefreshing) {
                    mCaptureController.refreshUserCaptureFeed(mUserId);
                } else {
                    mCaptureController.paginateUserCaptureFeed(mUserId);
                }
            } else {
                if (isRefreshing) {
                    mCaptureController.refreshFollowerFeed();
                } else {
                    mCaptureController.paginateFollowerFeed();
                }
            }
        }
        mIsLoadingData = true;
        mRefreshContainer.setRefreshing(true);
    }

    private void updateDisplayData() {
        mCaptureDetails.clear();
        if (mDetailsListing != null) {
            mCaptureDetails.addAll(mDetailsListing.getSortedCombinedData());
            Log.d(TAG, "Updating On Screen Data!");
        } else {
            // TODO: Emptystate for no data?
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void shouldLoadNextPage() {
        loadNextPage();
    }

    @Override
    public void dataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
