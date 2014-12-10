package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.CaptureDetailsAdapter;
import com.delectable.mobile.ui.common.widget.Delectabutton;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.NestedSwipeRefreshLayout;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.util.Animate;
import com.delectable.mobile.util.HideableActionBarScrollListener;
import com.delectable.mobile.util.SafeAsyncTask;
import com.melnykov.fab.FloatingActionButton;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CaptureListFragment extends BaseCaptureDetailsFragment implements
        InfiniteScrollAdapter.ActionsHandler {

    private static final String ACCOUNT_ID = "ACCOUNT_ID";

    private static final String LIST_KEY = "LIST_KEY";

    private static final String LIST_TITLE = "LIST_TITLE";

    private static final String LIST_BANNER = "LIST_BANNER";

    private static final String LIST_BANNER_BACKGROUND_COLOR = "LIST_BG_COLOR";

    private static final String LIST_BANNER_TEXT_COLOR = "LIST_TEXT_COLOR";

    private static final String TAG = CaptureListFragment.class.getSimpleName();

    private String LIST_REQUEST = TAG + "_list_req_";

    @InjectView(R.id.swipe_container)
    protected NestedSwipeRefreshLayout mRefreshContainer;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.camera_button)
    protected FloatingActionButton mCameraButton;

    protected View mEmptyView;

    protected View mEmptyViewBackground;

    @Inject
    protected CaptureListingModel mCaptureListingModel;

    protected CaptureDetailsAdapter mAdapter;

    private Listing<CaptureDetails, String> mCapturesListing;

    private boolean mFetching;

    protected String mListKey;

    protected String mTitle;

    protected String mBanner;

    protected int mBannerBackgroundColor;

    protected int mBannerTextColor;

    public CaptureListFragment() {
        // Required empty public constructor
    }

    public static CaptureListFragment newInstance(String accountId, String listKey,
            String listTitle, String banner, int bannerBackgroundColor, int bannerTextColor) {
        CaptureListFragment fragment = new CaptureListFragment();
        Bundle args = bundleArgs(accountId, listKey, listTitle, banner, bannerBackgroundColor,
                bannerTextColor);
        fragment.setArguments(args);
        return fragment;
    }

    protected static Bundle bundleArgs(String accountId, String listKey, String listTitle,
            String banner, int bannerBackgroundColor, int bannerTextColor) {
        Bundle args = new Bundle();
        args.putString(ACCOUNT_ID, accountId);
        args.putString(LIST_KEY, listKey);
        args.putString(LIST_TITLE, listTitle);
        args.putString(LIST_BANNER, banner);
        args.putInt(LIST_BANNER_BACKGROUND_COLOR, bannerBackgroundColor);
        args.putInt(LIST_BANNER_TEXT_COLOR, bannerTextColor);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        if (getArguments() == null) {
            throw new IllegalArgumentException(TAG + " needs to be initialized with a list key");
        }

        String accountId = getArguments().getString(ACCOUNT_ID);
        mListKey = getArguments().getString(LIST_KEY);
        LIST_REQUEST += mListKey;
        mTitle = getArguments().getString(LIST_TITLE);
        mBanner = getArguments().getString(LIST_BANNER);
        mBannerBackgroundColor = getArguments().getInt(LIST_BANNER_BACKGROUND_COLOR);
        mBannerTextColor = getArguments().getInt(LIST_BANNER_TEXT_COLOR);
        mAdapter = new CaptureDetailsAdapter(this, this, accountId);
        mAdapter.setRowType(CaptureDetailsAdapter.RowType.DETAIL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_feed_tab, container, false);
        ButterKnife.inject(this, view);

        mRefreshContainer.setListView(mListView);
        mRefreshContainer.setColorSchemeResources(R.color.d_chestnut);

        // consider ActionBar and TabStrip height for top padding
        mRefreshContainer.setProgressViewOffset(true, mListView.getPaddingTop() * 2,
                mListView.getPaddingTop() * 3);

        // list banner
        if (mBanner != null && !mBanner.isEmpty()) {
            TextView bannerView = (TextView) inflater
                    .inflate(R.layout.list_banner, mListView, false);
            bannerView.setText(mBanner);
            bannerView.setTextColor(mBannerTextColor);
            bannerView.setBackgroundColor(mBannerBackgroundColor);
            mListView.addHeaderView(bannerView);
            // adjust list padding on top, so list banner is not under tab bar
            int topPadding = mListView.getPaddingTop() + getResources()
                    .getDimensionPixelSize(R.dimen.tab_height);
            mListView.setPadding(0, topPadding, 0, 0);
        }

        mListView.setAdapter(mAdapter);

        // empty state
        if (mTitle.equalsIgnoreCase("following")) {
            View emptyViewContainer = view.findViewById(R.id.empty_view_following_container);
            mEmptyViewBackground = emptyViewContainer.findViewById(R.id.empty_view_following_logo);
            mEmptyView = emptyViewContainer.findViewById(R.id.empty_view_following);
            Delectabutton emptyViewButton = (Delectabutton) emptyViewContainer
                    .findViewById(R.id.search_friends_button);
            emptyViewButton.setIconDrawable(
                    getResources().getDrawable(R.drawable.ic_find_people_normal));
            emptyViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEventBus.post(new NavigationEvent(NavHeader.NAV_FIND_FRIENDS));
                }
            });
            mListView.setEmptyView(emptyViewContainer);
        } else {
            View emptyViewContainer = view.findViewById(R.id.empty_view_container);
            mEmptyViewBackground = emptyViewContainer.findViewById(R.id.empty_view_logo);
            mEmptyView = emptyViewContainer.findViewById(R.id.empty_view);
            mListView.setEmptyView(emptyViewContainer);
        }

        //pull to refresh setup
        mRefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Setup Floating Camera Button
        final HideableActionBarScrollListener hideableActionBarScrollListener
                = new HideableActionBarScrollListener(this);

        // Setup Floating Camera Button
        final FloatingActionButton.FabOnScrollListener fabOnScrollListener
                = new FloatingActionButton.FabOnScrollListener() {

            int lastVisibleItem = -1;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                hideableActionBarScrollListener
                        .onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                if (lastVisibleItem < firstVisibleItem) {
                    lastVisibleItem = firstVisibleItem;
                    mAnalytics.trackViewItemInFeed(getFeedName());
                }
            }
        };
        mCameraButton.attachToListView(mListView, fabOnScrollListener);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWineCapture();
            }
        });

        return view;
    }

    protected String getFeedName() {
        return mTitle;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getItems().isEmpty()) {
            loadLocalData();
        }
        if (!mCameraButton.isShown()) {
            mCameraButton.hide(false);
            mCameraButton.show(true);
        }
    }

    private void loadLocalData() {
        mRefreshContainer.setRefreshing(true);
        mFetching = true;
        new SafeAsyncTask<Listing<CaptureDetails, String>>(this) {
            @Override
            protected Listing<CaptureDetails, String> safeDoInBackground(Void[] params) {
                Log.d(TAG, "loadLocalData:doInBg");
                return getCachedFeed();
            }

            @Override
            protected void safeOnPostExecute(Listing<CaptureDetails, String> listing) {
                Log.d(TAG, "loadLocalData:returnedFromCache");
                if (listing != null) {
                    Log.d(TAG, "loadLocalData:listingExists size: " + listing.getUpdates().size());
                    mCapturesListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                }

                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    fetchCaptures(null, false);
                } else {
                    Log.d(TAG, "loadLocalData:success");
                    //simulate a pull to refresh if there are items
                    fetchCaptures(mCapturesListing, true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected Listing<CaptureDetails, String> getCachedFeed() {
        return mCaptureListingModel.getCaptureList(mListKey);
    }

    protected void fetchCaptures(Listing<CaptureDetails, String> listing,
            boolean isPullToRefresh) {
        mCaptureController
                .fetchCaptureList(LIST_REQUEST, mListKey, listing,
                        isPullToRefresh);
    }

    private void refreshData() {
        Log.d(TAG, "refreshData");

        if (mFetching) {
            return; //already fetching, don't load
        }
        mFetching = true;
        mRefreshContainer.setRefreshing(true);
        fetchCaptures(mCapturesListing, true);
    }

    public void onEventMainThread(UpdatedListingEvent<CaptureDetails, String> event) {
        if (!LIST_REQUEST.equals(event.getRequestId())) {
            return;
        }
        Log.d(TAG, "UpdatedListingEvent:reqMatch:" + event.getRequestId());
        if (event.getListing() != null) {
            Log.d(TAG, "UpdatedListingEvent:etag:" + event.getListing().getETag());
            Log.d(TAG, "UpdatedListingEvent:size:" + event.getListing().getUpdates().size());
        }
        mFetching = false;

        if (mRefreshContainer.isRefreshing()) {
            mRefreshContainer.setRefreshing(false);
            Log.d(TAG, "UpdatedListingEvent:wasRefreshing");
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }
        Log.d(TAG, "UpdatedListingEvent:eventSuccess");

        if (event.getListing() != null) {
            mCapturesListing = event.getListing();
            mAdapter.setItems(mCapturesListing.getUpdates());
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "UpdatedListingEvent:capturesExist");
        }
        //if cacheListing is null, means there are no updates
        //we don't let mFollowerListing get assigned null

        boolean showEmptyView = mAdapter.isEmpty();
        if (showEmptyView) {
            Animate.fadeOut(mEmptyViewBackground);
        }
        mEmptyView.setAlpha(showEmptyView ? 0 : 1);
        mEmptyView.setVisibility(showEmptyView ? View.VISIBLE : View.GONE);
        mEmptyView.animate().alpha(showEmptyView ? 1 : 0)
                .setInterpolator(new DecelerateInterpolator()).setDuration(300).start();
    }

    @Override
    public void shouldLoadNextPage() {
        Log.d(TAG, "shouldLoadNextPage");
        if (mFetching) {
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:notFetching");

        if (mCapturesListing == null) {
            //reached end of list/there are no items, we do nothing.
            //in theory, this should never be null because the getMore check below should stop it from loading
            return;
        }
        Log.d(TAG, "shouldLoadNextPage:captureListingExists");

        if (mCapturesListing.getMore()) {
            mFetching = true;
            fetchCaptures(mCapturesListing, false);
            Log.d(TAG, "shouldLoadNextPage:moreTrue");
        }
    }

    @Override
    public void reloadLocalData() {
        loadLocalData();
    }

    @Override
    public void dataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mAnalytics.trackSwitchFeed(mTitle);
        }
    }

}
