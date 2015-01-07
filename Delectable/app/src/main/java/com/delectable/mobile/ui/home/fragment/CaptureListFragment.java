package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.events.ui.HideOrShowFabEvent;
import com.delectable.mobile.api.events.wines.FetchedWineSourceEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureFeed;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.TransitionState;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.common.widget.CaptureDetailsAdapter;
import com.delectable.mobile.ui.common.widget.Delectabutton;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.NestedSwipeRefreshLayout;
import com.delectable.mobile.ui.common.widget.ObservableListView;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.util.Animate;
import com.delectable.mobile.util.HideableActionBarScrollListener;
import com.delectable.mobile.util.OnListScrollListener;
import com.delectable.mobile.util.SafeAsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CaptureListFragment extends BaseCaptureDetailsFragment implements
        InfiniteScrollAdapter.ActionsHandler {

    public static final String LIST_KEY = "LIST_KEY";

    public static final String LIST_TYPE = "LIST_TYPE";

    public static final String LIST_TITLE = "LIST_TITLE";

    private static final String LIST_BANNER = "LIST_BANNER";

    private static final String LIST_BANNER_BACKGROUND_COLOR = "LIST_BG_COLOR";

    private static final String LIST_BANNER_TEXT_COLOR = "LIST_TEXT_COLOR";

    private static final String TAG = CaptureListFragment.class.getSimpleName();

    private static final int NOT_FOUND = -1;

    private String LIST_REQUEST = TAG + "_list_req_";

    @InjectView(R.id.swipe_container)
    protected NestedSwipeRefreshLayout mRefreshContainer;

    @InjectView(R.id.list_view)
    protected ObservableListView mListView;

    protected View mEmptyView;

    protected View mEmptyViewBackground;

    @Inject
    protected BaseWineController mBaseWineController;

    @Inject
    protected CaptureListingModel mCaptureListingModel;

    protected CaptureDetailsAdapter mAdapter;

    private Listing<CaptureDetails, String> mCapturesListing;

    private boolean mFetching;

    protected String mListKey;

    protected String mListType;

    protected String mTitle;

    protected String mBanner;

    protected int mBannerBackgroundColor;

    protected int mBannerTextColor;

    public CaptureListFragment() {
        // Required empty public constructor
    }

    /**
     * For curated feeds (trending etc.)
     */
    public static CaptureListFragment newInstance(String listKey, String feedType,
            String listTitle, String banner, int bannerBackgroundColor, int bannerTextColor) {
        CaptureListFragment fragment = new CaptureListFragment();
        Bundle args = bundleArgs(listKey, feedType, listTitle, banner,
                bannerBackgroundColor,
                bannerTextColor);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * For custom feeds (hashtags etc.)
     */
    public static CaptureListFragment newInstance(String listKey, String listType,
            String listTitle) {
        CaptureListFragment fragment = new CaptureListFragment();
        Bundle args = bundleArgs(listKey, listType, listTitle);
        fragment.setArguments(args);
        return fragment;
    }

    protected static Bundle bundleArgs(String listKey, String listType, String listTitle) {
        Bundle args = new Bundle();
        args.putString(LIST_KEY, listKey);
        args.putString(LIST_TYPE, listType);
        args.putString(LIST_TITLE, listTitle);
        return args;
    }

    protected static Bundle bundleArgs(String listKey, String listType,
            String listTitle,
            String banner, int bannerBackgroundColor, int bannerTextColor) {
        Bundle args = new Bundle();
        args.putString(LIST_KEY, listKey);
        args.putString(LIST_TYPE, listType);
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

        mListKey = getArguments().getString(LIST_KEY);
        mListType = getArguments().getString(LIST_TYPE);
        LIST_REQUEST += mListKey;
        mTitle = getArguments().getString(LIST_TITLE);
        mBanner = getArguments().getString(LIST_BANNER);
        mBannerBackgroundColor = getArguments().getInt(LIST_BANNER_BACKGROUND_COLOR);
        mBannerTextColor = getArguments().getInt(LIST_BANNER_TEXT_COLOR);
        mAdapter = new CaptureDetailsAdapter(this, this);
        mAdapter.setRowType(CaptureFeed.COMMERCIAL.equals(mListType)
                ? CaptureDetailsAdapter.RowType.COMMERCIAL
                : CaptureDetailsAdapter.RowType.SOCIAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_feed_tab, container, false);
        ButterKnife.inject(this, view);

        mRefreshContainer.setListView(mListView);
        mRefreshContainer.setColorSchemeResources(R.color.d_chestnut);

        int topPadding = 0;
        if (CaptureFeed.CUSTOM.equals(mListType)) {
            // simple list padding when feed is in it's own activity
            topPadding = getResources().getDimensionPixelOffset(R.dimen.spacing_8);
        } else {
            // account for toolbar and tabbar height on embedded feed lists
            topPadding = mListView.getPaddingTop() + getResources()
                    .getDimensionPixelSize(R.dimen.tab_height) + getResources()
                    .getDimensionPixelSize(R.dimen.spacing_4);
            mRefreshContainer.setProgressViewOffset(true, mListView.getPaddingTop() * 2,
                    mListView.getPaddingTop() * 3);
        }
        mListView.setPadding(mListView.getPaddingLeft(), topPadding,
                mListView.getPaddingRight(), mListView.getPaddingBottom());

        // list banner
        if (mBanner != null && !mBanner.isEmpty()) {
            View bannerView = inflater.inflate(R.layout.list_banner, mListView, false);
            TextView bannerText = (TextView) bannerView.findViewById(R.id.list_banner_text);
            CardView bannerCard = (CardView) bannerView.findViewById(R.id.card);
            bannerText.setText(mBanner);
            bannerText.setTextColor(mBannerTextColor);
            bannerCard.setCardBackgroundColor(mBannerBackgroundColor);
            mListView.addHeaderView(bannerView);
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

        // action bar scroll listener for embedded feeds
        if (!CaptureFeed.CUSTOM.equals(mListType)) {
            final HideableActionBarScrollListener hideableActionBarScrollListener
                    = new HideableActionBarScrollListener(this);
            mListView.addOnScrollListener(hideableActionBarScrollListener);
        }

        // analytics scroll listener
        mListView.addOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastVisibleItem = -1;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                if (lastVisibleItem < firstVisibleItem) {
                    lastVisibleItem = firstVisibleItem;
                    mAnalytics.trackViewItemInFeed(getFeedName());
                }
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
            mEventBus.post(new HideOrShowFabEvent(true));
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
            // scroll listener for FAB after adapter is populated (otherwise there is an unwanted scroll event)
            mListView.addOnScrollListener(
                    new OnListScrollListener(new OnListScrollListener.OnScrollDirectionListener() {
                        @Override
                        public void onScrollUp() {
                            mEventBus.post(new HideOrShowFabEvent(false));
                        }

                        @Override
                        public void onScrollDown() {
                            mEventBus.post(new HideOrShowFabEvent(true));
                        }
                    }));

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
    public void checkPrice(CaptureDetails capture, CaptureDetailsView view) {

        //fetch wine source
        if (capture.getWineProfile()!=null) {

            //immediate set these values for object on hand for immediate UI consumption, job will also set these in the model layer
            capture.setTransacting(true);
            capture.setTransitionState(TransitionState.UPDATING);
            capture.setTransactionKey(CaptureDetails.TRANSACTION_KEY_PRICE);
            mBaseWineController.fetchWineSource(capture.getId(), capture.getWineProfile().getId(), null);
        }
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

    public void onEventMainThread(FetchedWineSourceEvent event) {
        //TODO may need extra filter here to differentiate between fetch wine source event here and from wine profile

        //we always provide capture id when call fetchWineSource from this fragment
        if (event.getCaptureId() == null) {
            return;
        }
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }

        //replace item
        int position = NOT_FOUND;
        boolean foundCapture = false;
        for(int i = 0; i < mAdapter.getItems().size(); i++) {
            CaptureDetails capture = mAdapter.getItems().get(i);
            if (capture.getId().equals(event.getCaptureId())) {
                position = i;
                foundCapture = true;
                break;
            }
        }
        if (foundCapture) {
            mAdapter.getItems().set(position, event.getCaptureDetails());
        }

        //finally, update listview display
        mAdapter.notifyDataSetChanged();
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
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            // this fragment is fully visible (in ViewPager)
            if (mAnalytics != null) {
                mAnalytics.trackSwitchFeed(mTitle);
            }

            if (mAdapter != null && mAdapter.isEmpty()) {
                // always reveal FAB and toolbar on empty feeds, because the user cannot scroll
                mEventBus.post(new HideOrShowFabEvent(true));
                if (getBaseActivity() != null) {
                    getBaseActivity().showOrHideActionBar(true);
                }
            }
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            mAnalytics.trackSwitchFeed(mTitle);
//        }
//    }

}
