package com.delectable.mobile.ui.navigation.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.ActivityRecipient;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.api.requests.AccountsContextRequest;
import com.delectable.mobile.api.requests.ActivityFeedRequest;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.ActivityFeedAdapter;
import com.delectable.mobile.ui.navigation.widget.NavHeader;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NavigationDrawerFragment extends BaseFragment implements
        NavHeader.NavHeaderActionListener {

    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private View mView;

    private DrawerLayout mDrawerLayout;

    private ListView mDrawerListView;

    private View mFragmentContainerView;

    private ActivityFeedAdapter mActivityFeedAdapter;

    private NavHeader mNavHeader;

    private String mUserId;

    private AccountsNetworkController mAccountsNetworkController;

    private BaseNetworkController mBaseNetworkController;

    private Account mUserAccount;

    private ArrayList<ActivityRecipient> mActivityRecipients;

    private ListingResponse<ActivityRecipient> mActivityRecipientListing;

    private int mCurrentSelectedNavItem = 0;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRecipients = new ArrayList<ActivityRecipient>();
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
        mBaseNetworkController = new BaseNetworkController(getActivity());
        mUserId = UserInfo.getUserId(getActivity());

        if (savedInstanceState != null) {
            mCurrentSelectedNavItem = savedInstanceState.getInt(STATE_SELECTED_POSITION);
        }

        selectItem(mCurrentSelectedNavItem);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // This may seem redundant, but doing it this way prevents annoying crashes when refactoring and forgetting to change the return type
        mDrawerListView = (ListView) mView;

        mNavHeader = new NavHeader(getActivity());
        mDrawerListView.addHeaderView(mNavHeader);
        mNavHeader.setActionListener(this);

        mActivityFeedAdapter = new ActivityFeedAdapter(getActivity(), mActivityRecipients);

        mDrawerListView.setAdapter(mActivityFeedAdapter);
        mNavHeader.setCurrentSelectedNavItem(mCurrentSelectedNavItem);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
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
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
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
    }

    private void selectItem(int position) {
        mCurrentSelectedNavItem = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    private void loadData() {
        loadUserData();
        loadActivityFeed();
    }

    private void loadUserData() {
        // TODO: Have some central data loading service and cache this somewhere
        AccountsContextRequest request = new AccountsContextRequest(
                AccountsContextRequest.CONTEXT_PROFILE);
        request.setId(mUserId);
        mAccountsNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        Log.d(TAG, "Received Results! " + result);

                        mUserAccount = (Account) result;
                        updateUIWithData();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        Log.d(TAG, "Results Failed! " + error.getMessage() + " Code:" + error
                                .getCode());
                        // TODO: What to do with errors?
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void loadActivityFeed() {
        ActivityFeedRequest request = new ActivityFeedRequest();
        mBaseNetworkController.performRequest(request, new BaseNetworkController.RequestCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                Log.d(TAG, "Result: " + result);
                mActivityRecipientListing = (ListingResponse<ActivityRecipient>) result;
                mActivityRecipients.clear();
                if (mActivityRecipientListing != null) {
                    mActivityRecipients.addAll(mActivityRecipientListing.getSortedCombinedData());
                } else {
                    // TODO: Empty State for no data?
                }
                mActivityFeedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(RequestError error) {
                Log.e(TAG, "Error Result? : " + error);
                showToastError(error.getMessage());
            }
        });
    }

    private void updateUIWithData() {
        if (mUserAccount == null) {
            return;
        }
        mNavHeader.setFollowerCount(mUserAccount.getFollowerCount());
        mNavHeader.setFollowingCount(mUserAccount.getFollowingCount());
        mNavHeader.setUserName(mUserAccount.getFullName());
        mNavHeader.setUserBio(mUserAccount.getBio());
        // TODO: Calculate Recent scans count somehow and store it
        mNavHeader.setRecentScansCount(0);
        ImageLoaderUtil.loadImageIntoView(getActivity(), mUserAccount.getPhoto().getUrl(),
                mNavHeader.getUserImageView());
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    @Override
    public void navHeaderUserImageClicked() {
        // TODO: Figure out whether we open in Activity or in Fragment with Nav
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, mUserId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void navItemClicked(int navItem) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(navItem);
        }
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
