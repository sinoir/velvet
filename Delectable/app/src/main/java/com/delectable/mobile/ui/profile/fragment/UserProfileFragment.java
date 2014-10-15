package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.accounts.FollowAccountEvent;
import com.delectable.mobile.events.accounts.UpdatedAccountProfileEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.ObservableScrollView;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;
import com.delectable.mobile.ui.profile.widget.ProfileHeaderView;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

public class UserProfileFragment extends BaseFragment implements
        ProfileHeaderView.ProfileHeaderActionListener, ObservableScrollView.Callbacks,
        RecentCapturesTabFragment.Callback {

    private static final String TAG = UserProfileFragment.class.getSimpleName();

    private static final String sArgsUserId = "sArgsUserId";

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected AccountModel mAccountModel;

    private View mView;

    private ProfileHeaderView mProfileHeaderView;

    private ViewPager mViewPager;

    private SlidingPagerTabStrip mTabStrip;

    private SlidingPagerAdapter mTabsAdapter;

    private AccountProfile mUserAccount;

    private String mUserId;

    private ObservableScrollView mScrollView;

    private View mContainer;

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            resizeContainer();
        }
    };

    private int mLastScrollY;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(sArgsUserId, userId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Resize the Container View within the Scroll to allow scrolling.
     */
    private void resizeContainer() {
        int scrollHeight = mScrollView.getMeasuredHeight();
        int headerHeight = mProfileHeaderView.getMeasuredHeight();
        int middleHeight = mTabStrip.getMeasuredHeight();

        // Setup Container Height
        int currentContainerHeight = mContainer.getMeasuredHeight();
        int newContainerHeight = scrollHeight + headerHeight;
        if (newContainerHeight != currentContainerHeight) {
            ViewGroup.LayoutParams params = mContainer.getLayoutParams();
            params.height = newContainerHeight;
            mContainer.setLayoutParams(params);
        }

        // Setup ListView height to fill the rest
        int currentListHeight = mViewPager.getMeasuredHeight();
        int newListHeight = newContainerHeight - (headerHeight + middleHeight);
        if (newListHeight != currentListHeight) {
            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.height = newListHeight;
            mViewPager.setLayoutParams(params);
        }
        // Scroll to last position, if it was just created, it'll be 0.  Otherwise it'll scroll
        // randomly to the bottom, and back to top when the UI redraws, such as when selecting
        // different tabs
        mScrollView.setScrollY(mLastScrollY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getString(sArgsUserId);
        }
        mLastScrollY = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mScrollView = (ObservableScrollView) mView;

        mProfileHeaderView = (ProfileHeaderView) mView.findViewById(R.id.profile_header_view);
        mProfileHeaderView.setActionListener(this);

        mContainer = mView.findViewById(R.id.container);
        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabStrip = (SlidingPagerTabStrip) mView.findViewById(R.id.tabstrip);

        ArrayList<SlidingPagerAdapter.SlidingPagerItem> tabItems
                = new ArrayList<SlidingPagerAdapter.SlidingPagerItem>();

        // "RECENT" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                RecentCapturesTabFragment.newInstance(mUserId),
                R.color.d_off_white,
                R.color.d_chestnut,
                R.color.dark_gray_to_chestnut,
                getString(R.string.profile_tab_recent)));

        // "TOP RATED" tab
        // TODO: Replace with TOP Rated or whatever other tabs
//        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
//                RecentCapturesTabFragment.newInstance(mUserId),
//                R.color.d_off_white,
//                R.color.d_chestnut,
//                R.color.dark_gray_to_chestnut,
//                getString(R.string.profile_tab_top_rated)));

        // TODO: Unhide Indicator when we implement another tab
        mTabStrip.setVisibility(View.GONE);

        mTabsAdapter = new SlidingPagerAdapter(getFragmentManager(), tabItems);

        mViewPager.setAdapter(mTabsAdapter);
        mTabStrip.setViewPager(mViewPager);

        setupCustomScrolling();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        // Must reassign the helper listview callbacks because of inner fragments get reinitialized
        // from a saved state.
        getRecentCapturesTabFragment().setCallback(this);
        // TODO: Replace this once we add another Tab
//        getTopRatedTabFragment().setCallback(this);

        // Update User Private account info as well
        // TODO: Need to store this as 1 object, storing duplciate account info is causing weird issues.
        if (mUserId != null && mUserId.equals(UserInfo.getUserId(getActivity()))) {
            mAccountController.fetchAccountPrivate(mUserId);
        }
        // fetch profile to check for updates (we're using eTags, so no big deal)
        mAccountController.fetchProfile(mUserId);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO: Custom Back Arrow...
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                launchFindPeople();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private RecentCapturesTabFragment getRecentCapturesTabFragment() {
        return (RecentCapturesTabFragment) mTabsAdapter.getItem(0);
    }

    private RecentCapturesTabFragment getTopRatedTabFragment() {
        return (RecentCapturesTabFragment) mTabsAdapter.getItem(1);
    }

    /**
     * Sets up scroll observers and such.
     */
    private void setupCustomScrolling() {
        mScrollView.setCallbacks(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    private void launchFindPeople() {
        // TODO: Find People screen
        Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
/*
        new SafeAsyncTask<Account>(this) {
            @Override
            protected Account safeDoInBackground(Void[] params) {
                return mAccountModel.getAccount(mUserId);
            }

            @Override
            protected void safeOnPostExecute(Account account) {
                mUserAccount = account;
                if (mUserAccount != null) {
                    mCaptureDetails.clear();
                    if (mUserAccount.getCaptureSummaries() != null
                            && mUserAccount.getCaptureSummaries().size() > 0) {
                        for (CaptureSummary summary : mUserAccount.getCaptureSummaries()) {
                            mCaptureDetails.addAll(summary.getCaptures());
                        }
                    }
                    updateUIWithData();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/

        // FIXME Use asynchronous method above once the other requests are refactored as well
        // and see if the UI speed will improve, otherwise we might stick with the
        // synchronous retrieval for small data
        mUserAccount = mAccountModel.getAccount(mUserId);
        if (mUserAccount != null) {
            Log.d(TAG, "CACHE HIT for profile: " + mUserId);
            updateUIWithData();
        }

    }

    //region EventBus Events
    public void onEventMainThread(UpdatedAccountProfileEvent event) {
        if (!mUserId.equals(event.getAccount().getId())) {
            return;
        }

        if (event.isSuccessful()) {
            mUserAccount = event.getAccount();
            updateUIWithData();
            return;
        }
        showToastError(event.getErrorMessage());
    }


    public void onEventMainThread(FollowAccountEvent event) {
        //follow account job wasn't fired from this fragment
        if (!mUserId.equalsIgnoreCase(event.getAccountId())) {
            return;
        }

        // Reload Data
        loadData();
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
        } else if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }
    }
    //endregion

    @Override
    public void toggleFollowUserClicked(boolean isFollowingSelected) {
        // Update Count
        int followerCountDiff = isFollowingSelected ? 1 : -1;
        mUserAccount.setFollowerCount(mUserAccount.getFollowerCount() + followerCountDiff);
        int relationship = isFollowingSelected ? AccountProfile.RELATION_TYPE_FOLLOWING
                : AccountProfile.RELATION_TYPE_NONE;
        mUserAccount.setCurrentUserRelationship(relationship);
        updateUIWithData();
        mAccountController.followAccount(mUserId, isFollowingSelected);
    }

    private void updateUIWithData() {
        mProfileHeaderView.setDataToView(mUserAccount);
    }

    @Override
    public void wineCountClicked() {
        // TODO: Do we do anything?
    }

    @Override
    public void followerCountClicked() {
        launchNextFragment(FollowersFragment.newInstance(mUserId));
    }

    @Override
    public void followingCountClicked() {
        launchNextFragment(FollowingFragment.newInstance(mUserId));

    }

    @Override
    public void onScrollChanged(int deltaX, int deltaY) {
        // no-op

    }

    @Override
    public void onScrollViewOverScrollBy(int deltaY, int scrollY, boolean isTouchEvent,
            boolean overScrollResult) {
        // When Overscrolling on the bottom of the main ScrollView, we lock the scroll view, this
        // snaps the mid section to the top, since the height of the container is extended by
        // the height of the header and allows us to start scrolling the list view
        mLastScrollY = scrollY;
        if (overScrollResult && deltaY > 0) {
            mScrollView.setScrollingCanceled(true);
        }
    }

    @Override
    public void onCaptureListOverScrolledTop() {
        // Once the ListView overscrolls to the top, show the header by scrolling to the top.
        mScrollView.setScrollingCanceled(false);
        mLastScrollY = 0;
        mScrollView.smoothScrollTo(0, 0);
    }
}
