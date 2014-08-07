package com.delectable.mobile.ui.profile.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureSummary;
import com.delectable.mobile.api.requests.FollowAccountsActionRequest;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.events.FetchAccountFailedEvent;
import com.delectable.mobile.events.FetchedAccountEvent;
import com.delectable.mobile.model.local.Account;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.widget.SlidingPagerAdapter;
import com.delectable.mobile.ui.common.widget.SlidingPagerTabStrip;
import com.delectable.mobile.ui.profile.widget.ProfileHeaderView;
import com.delectable.mobile.util.ImageLoaderUtil;
import com.delectable.mobile.util.SafeAsyncTask;

import java.util.ArrayList;

import javax.inject.Inject;

public class UserProfileFragment extends BaseFragment implements
        ProfileHeaderView.ProfileHeaderActionListener {

    public static final String TAG = "UserProfileFragment";

    private static final String sArgsUserId = "sArgsUserId";

    private View mView;

    private ProfileHeaderView mProfileHeaderView;

    private ViewPager mViewPager;

    private SlidingPagerTabStrip mTabStrip;

    private SlidingPagerAdapter mTabsAdapter;

    private BaseNetworkController mNetworkController;

    private Account mUserAccount;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private String mUserId;

    @Inject
    AccountController mAccountController;
    @Inject
    AccountModel mAccountModel;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        mCaptureDetails = new ArrayList<CaptureDetails>();
        mNetworkController = new AccountsNetworkController(getActivity());
        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getString(sArgsUserId);
        }

        // fetch profile to check for updates (we're using eTags, so no big deal)
        mAccountController.fetchProfile(mUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        mView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mProfileHeaderView = (ProfileHeaderView) mView.findViewById(R.id.profile_header_view);
        mProfileHeaderView.setActionListener(this);

        mViewPager = (ViewPager) mView.findViewById(R.id.pager);
        mTabStrip = (SlidingPagerTabStrip) mView.findViewById(R.id.tabstrip);

        ArrayList<SlidingPagerAdapter.SlidingPagerItem> tabItems
                = new ArrayList<SlidingPagerAdapter.SlidingPagerItem>();

        // TODO: Split Recent / TopRated Tabs

        // "RECENT" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                RecentCapturesTabFragment.newInstance(mUserId),
                R.color.d_dark_navy,
                R.color.d_light_green,
                R.color.tab_text_white_grey,
                getString(R.string.profile_tab_recent)));

        // "TOP RATED" tab
        tabItems.add(new SlidingPagerAdapter.SlidingPagerItem(
                RecentCapturesTabFragment.newInstance(mUserId),
                R.color.d_dark_navy,
                R.color.d_light_green,
                R.color.tab_text_white_grey,
                getString(R.string.profile_tab_top_rated)));

        mTabsAdapter = new SlidingPagerAdapter(getFragmentManager(), tabItems);

        mViewPager.setAdapter(mTabsAdapter);
        mTabStrip.setViewPager(mViewPager);

        return mView;
    }

    private RecentCapturesTabFragment getRecentCapturesTabFragment() {
        return (RecentCapturesTabFragment) mTabsAdapter.getItem(0);
    }

    private RecentCapturesTabFragment getTopRatedTabFragment() {
        return (RecentCapturesTabFragment) mTabsAdapter.getItem(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
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
            case R.id.toggle_list:
                toggleListDetailView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleListDetailView() {
        // TODO: Toggle Menu Icon
        getRecentCapturesTabFragment().toggleAdapterViewState();
        getTopRatedTabFragment().toggleAdapterViewState();
    }

    private void launchFindPeople() {
        // TODO: Find People screen
        Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {

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
                // Fetch profile to check for updates (we're using eTags, so no big deal)
                mAccountController.fetchProfile(mUserId);
            }
        }.execute();

        /*
        // Synchronous
        mUserAccount = mAccountModel.getAccount(mUserId);
        if (mUserAccount != null) {
            Log.d(TAG, "CACHE HIT for profile: " + mUserId);
            mCaptureDetails.clear();
            if (mUserAccount.getCaptureSummaries() != null
                    && mUserAccount.getCaptureSummaries().size() > 0) {
                for (CaptureSummary summary : mUserAccount.getCaptureSummaries()) {
                    mCaptureDetails.addAll(summary.getCaptures());
                }
            }
            updateUIWithData();
        }
        */

    }

    public void onEventMainThread(FetchedAccountEvent event) {
        if (!mUserId.equals(event.getAccountId()))
            return;
        loadData();
    }

    public void onEventMainThread(FetchAccountFailedEvent event) {
        // TODO show error dialog
    }

    private void updateUIWithData() {

        String userName = mUserAccount.getFname() + " " + mUserAccount.getLname();
        String imageUrl = mUserAccount.getPhoto().getUrl();
        int numCaptures = mUserAccount.getCaptureCount() != null ?
                mUserAccount.getCaptureCount() : 0;

        mProfileHeaderView.setWineCount(numCaptures);

        ImageLoaderUtil.loadImageIntoView(getActivity(), imageUrl,
                mProfileHeaderView.getUserImageView());
        mProfileHeaderView.setUserName(userName);
        mProfileHeaderView.setFollowerCount(mUserAccount.getFollowerCount());
        mProfileHeaderView.setFollowingCount(mUserAccount.getFollowingCount());
        mProfileHeaderView.setUserBio(mUserAccount.getBio());

        if (mUserAccount.isUserRelationshipTypeSelf()) {
            mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_SELF);
        } else if (mUserAccount.isUserRelationshipTypeFollowing()) {
            mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_FOLLOWING);
        } else {
            mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_NOT_FOLLOWING);
        }
    }

    @Override
    public void toggleFollowUserClicked(final boolean isFollowingSelected) {
        Log.d(TAG, "Toggle Following? " + isFollowingSelected);
        FollowAccountsActionRequest request = new FollowAccountsActionRequest(mUserId,
                isFollowingSelected);
        mNetworkController.performRequest(request, new BaseNetworkController.RequestCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                // Do nothing
                Log.d(TAG, "Toggle Follow Success! " + result);
            }

            @Override
            public void onFailed(RequestError error) {
                showToastError(error.getMessage());
                // Revert Follow Button state
                // If the user toggled to Is Following, should revert back to not Following
                if (isFollowingSelected) {
                    mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_NOT_FOLLOWING);
                } else {
                    mProfileHeaderView.setFollowingState(ProfileHeaderView.STATE_FOLLOWING);
                }
            }
        });
    }

    @Override
    public void wineCountClicked() {
        // TODO: Do we do anything?
    }

    @Override
    public void followerCountClicked() {
        // TODO: Do we do anything?
    }

    @Override
    public void followingCountClicked() {
        // TODO: Do we do anything?
    }
}
