package com.delectable.mobile.ui.navigation.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.events.accounts.FollowAccountEvent;
import com.delectable.mobile.api.events.accounts.UpdatedAccountEvent;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.ActivityFeedItem;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.ActivityFeedAdapter;
import com.delectable.mobile.ui.events.NavigationDrawerCloseEvent;
import com.delectable.mobile.ui.events.NavigationEvent;
import com.delectable.mobile.ui.navigation.widget.ActivityFeedRow;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.util.AnalyticsUtil;
import com.delectable.mobile.util.DeepLink;
import com.delectable.mobile.util.FacebookEventUtil;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import javax.inject.Inject;


public class NavigationDrawerFragment extends BaseFragment implements
        NavHeader.NavHeaderActionListener, ActivityFeedRow.ActivityActionsHandler,
        AdapterView.OnItemClickListener {

    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();

    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    private static final String ACTIVITY_FEED_REQ = TAG + "_activity_feed";

    private static final String FETCH_ACCOUNT = TAG + "_fetch_account";

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    @Inject
    protected AccountController mAccountController;

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;

    private View mContainerView;

    private ListView mDrawerListView;

    private View mEmptyView;

    private ActivityFeedAdapter mActivityFeedAdapter = new ActivityFeedAdapter(this);

    private NavHeader mNavHeader;

    private String mUserId;

    private int mCurrentSelectedNavItem = 0;

    private Account mUserAccount;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        setHasOptionsMenu(true);
        mUserId = UserInfo.getUserId(getActivity());

        if (savedInstanceState != null) {
            mCurrentSelectedNavItem = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(mCurrentSelectedNavItem);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // This may seem redundant, but doing it this way prevents annoying crashes when refactoring and forgetting to change the return type
        mDrawerListView = (ListView) view;

        mNavHeader = new NavHeader(getActivity());
        mDrawerListView.addHeaderView(mNavHeader, null, false);
        mNavHeader.setActionListener(this);

        mEmptyView = inflater.inflate(R.layout.navigation_empty_view, mDrawerListView, false);
        mDrawerListView.addFooterView(mEmptyView, null, false);

        mDrawerListView.setOnItemClickListener(this);
        mDrawerListView.setAdapter(mActivityFeedAdapter);
        mNavHeader.setCurrentSelectedNavItem(mCurrentSelectedNavItem);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.nav_menu, menu);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mActivityFeedAdapter.isEmpty()) {
            mDrawerListView.removeFooterView(mEmptyView);
        }

        mUserAccount = UserInfo.getAccountPrivate(getActivity());
        // Must update / sync private account on Resume, for sycning issues.
        // TODO: Need to create 1 object for private / public, otherwise we require special syncing code for the duplciate data, which doesn't exist.
        mAccountController.fetchAccountPrivate(FETCH_ACCOUNT);
        updateUIWithData();
        loadActivityFeed();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedNavItem);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
//        mDrawerLayout
//                .setStatusBarBackgroundColor(getResources().getColor(R.color.overlayed_status_bar));
        mContainerView = getActivity().findViewById(R.id.drawer_container);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
                showOrHideActionBar(true);
            }
        };

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setElevation(mContainerView, TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                            getResources().getDisplayMetrics()));
        } else {
            mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        }

        // When the user runs the app for the first time, we want to land them with the
        // navigation drawer open. But just the first time.
        if (!UserInfo.isWelcomeDone()) {
            // first run of the app starts with the nav drawer open
            UserInfo.markWelcomeDone();
            mDrawerLayout.openDrawer(Gravity.START);

            FacebookEventUtil.logFirstAppUse(getActivity());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position--; //offset bc of header
        ActivityFeedItem feedItem = mActivityFeedAdapter.getItem(position);

        // check ID prefix for activity type
        // TODO watch out for additional activity feed types and changes!
        String itemId = feedItem.getId();
        if (itemId != null) {
            String activityType = null;
            if (itemId.startsWith("Follow")) {
                activityType = AnalyticsUtil.ACTIVITY_FOLLOW;
            } else if (itemId.startsWith("FixedTranscription")) {
                activityType = AnalyticsUtil.ACTIVITY_TRANSCRIPTION;
            } else if (itemId.startsWith("IdentifiedTranscription")) {
                activityType = AnalyticsUtil.ACTIVITY_TRANSCRIPTION;
            } else if (itemId.startsWith("Helpful")) {
                activityType = AnalyticsUtil.ACTIVITY_HELPFUL;
            } else if (itemId.startsWith("Like")) {
                activityType = AnalyticsUtil.ACTIVITY_LIKE;
            } else if (itemId.startsWith("NewAccount")) {
                activityType = AnalyticsUtil.ACTIVITY_NEWACCOUNT;
            } else if (itemId.startsWith("Tagged")) {
                activityType = AnalyticsUtil.ACTIVITY_TAGGING;
            } else if (itemId.startsWith("Comment")) {
                activityType = AnalyticsUtil.ACTIVITY_COMMENT;
            }
            mAnalytics.trackActivity(activityType);
        }

        //let click on row be absorbed by rightImageLink first. if that's null, then let the leftImageLink handle it.
        if (feedItem.getRightImageLink() != null && feedItem.getRightImageLink().getUrl() != null) {
            final String rightImageUrl = feedItem.getRightImageLink().getUrl();
            openDeepLink(rightImageUrl);
            return;
        }

        if (feedItem.getLeftImageLink() != null && feedItem.getLeftImageLink().getUrl() != null) {
            final String leftImageUrl = feedItem.getLeftImageLink().getUrl();
            openDeepLink(leftImageUrl);
        }

    }

    //region EventBus events
    public void onEventMainThread(UpdatedAccountEvent event) {
        //no need to filter by request id, this listens to all events

        mUserAccount = event.getAccount();
        updateUIWithData();

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }

        // Update Push notification stuff after user logs in / updates account.
        // TODO: Put this somewhere else that makes more sense..
        try {
            App.getInstance().updateKahunaAttributes();
        } catch (Exception ex) {
            Log.wtf(TAG, "Kahuna Failed", ex);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(FollowAccountEvent event) {
        if (!event.isSuccessful() || mUserAccount == null || getActivity() == null) {
            return;
        }
        // Update User Account when following someone new
        mUserAccount = UserInfo.getAccountPrivate(getActivity());
        updateUIWithData();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(NavigationDrawerCloseEvent event) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mContainerView);
        }
    }

    public void onEventMainThread(UpdatedListingEvent<ActivityFeedItem, String> event) {
        if (!event.getRequestId().equals(ACTIVITY_FEED_REQ)) {
            return;
        }
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }

        //TODO optimized to use etag
        if (event.getListing() != null) {
            Listing<ActivityFeedItem, String> mActivityRecipientListing = event.getListing();
            mActivityFeedAdapter.setItems(mActivityRecipientListing.getUpdates());
            mActivityFeedAdapter.notifyDataSetChanged();
            if (!mActivityFeedAdapter.isEmpty()) {
                mDrawerListView.removeFooterView(mEmptyView);
            }

        }
    }

    public void onEventMainThread(NavigationEvent event) {
        mNavHeader.setCurrentSelectedNavItem(event.itemPosition);
        navItemSelected(event.itemPosition);
    }

    //endregion

    private void loadActivityFeed() {
        mAccountController.fetchActivityFeed(ACTIVITY_FEED_REQ, null, null, null);
    }

    private void updateUIWithData() {
        if (mUserAccount == null) {
            return;
        }
        mNavHeader.setWineCount(mUserAccount.getCaptureCount());
        mNavHeader.setUserName(mUserAccount.getFullName());
        ImageLoaderUtil.loadImageIntoView(getActivity(), mUserAccount.getPhoto().getBestThumb(),
                mNavHeader.getUserImageView());
    }

    @Override
    public void navHeaderUserImageClicked() {
        // TODO launch user profile with taste profile as the default page, "your wines" brings you to the wine list
        startActivity(UserProfileActivity.newIntent(getActivity(), mUserId));
    }

    @Override
    public void navItemSelected(final int navItem) {
        boolean wasNavAlreadySelected = mCurrentSelectedNavItem == navItem;
        mCurrentSelectedNavItem = navItem;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mContainerView);
        }
        if (mCallbacks != null && !wasNavAlreadySelected) {
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mCallbacks.onNavigationDrawerItemSelected(navItem);
                }
            }, NAVDRAWER_LAUNCH_DELAY);
        }
    }

    @Override
    public void openDeepLink(String url) {
        Uri deepLinkUri = Uri.parse(url);
        Intent intent = DeepLink.getIntent(getActivity(), deepLinkUri);
        if (intent != null) {
            startActivity(intent);
        }

        //TODO account for opening intent that opens into NavActivity
        //for now I haven't seen the feed deeplink appear in recent activity  but
        //might need to account for delectable://feed which opens something in navactivity, the current activity that we're already in
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {

        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

}