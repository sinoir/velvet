package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.AccountModel;
import com.delectable.mobile.api.cache.CapturesPendingCapturesListingModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.events.accounts.FollowAccountEvent;
import com.delectable.mobile.api.events.accounts.UpdatedAccountProfileEvent;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.profile.activity.FollowersFollowingActivity;
import com.delectable.mobile.ui.profile.widget.CapturesPendingCapturesAdapter;
import com.delectable.mobile.ui.profile.widget.MinimalPendingCaptureRow;
import com.delectable.mobile.ui.profile.widget.ProfileHeaderView;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;
import com.delectable.mobile.util.SafeAsyncTask;
import com.melnykov.fab.FloatingActionButton;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserProfileFragment extends BaseCaptureDetailsFragment implements
        ProfileHeaderView.ProfileHeaderActionListener, InfiniteScrollAdapter.ActionsHandler,
        MinimalPendingCaptureRow.ActionsHandler {

    private static final String TAG = UserProfileFragment.class.getSimpleName();

    private static final String USER_ID = "USER_ID";

    private static final String CAPTURES_REQ = TAG + "_captures_req";

    @Inject
    protected CapturesPendingCapturesListingModel mListingModel;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    @InjectView(R.id.empty_state_layout)
    protected View mEmptyStateLayout;

    @InjectView(R.id.empty_view_header)
    protected ProfileHeaderView mEmptyStateHeader;

    /**
     * In the layout, this covers the loading circle complete when it's set to visible, so there's
     * no need to hide the loading circle.
     */
    @InjectView(R.id.nothing_to_display_textview)
    protected FontTextView mNoCapturesTextView;

    @InjectView(R.id.camera_button)
    protected FloatingActionButton mCameraButton;

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected AccountModel mAccountModel;

    private CapturesPendingCapturesAdapter mAdapter;

    private Listing<BaseListingElement> mCapturesListing;

    private boolean mFetching;

    private String mEmptyStateText;

    private ProfileHeaderView mProfileHeaderView;

    private AccountProfile mUserAccount;

    private String mUserId;

    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance(String userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getString(USER_ID);
            mAdapter = new CapturesPendingCapturesAdapter(this, this, this, mUserId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = View.inflate(getActivity(), R.layout.fragment_user_profile, null);
        ButterKnife.inject(this, view);

        setEmptyStateText(mEmptyStateText);

        mProfileHeaderView = (ProfileHeaderView) inflater
                .inflate(R.layout.profile_header_impl, mListView, false);
        mProfileHeaderView.setActionListener(this);

        mEmptyStateHeader.setActionListener(this);

        mListView.setAdapter(mAdapter);
        mListView.addHeaderView(mProfileHeaderView);
        // Does not work with list header
        mListView.setEmptyView(mEmptyStateLayout);

        // Setup Floating Camera Button
        mCameraButton.attachToListView(mListView);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWineCapture();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

        // Update User Private account info as well
        // TODO: Need to store this as 1 object, storing duplicate account info is causing weird issues.
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

        if (mAdapter.getItems().isEmpty()) {
            loadLocalData();
        }
    }

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

    public void onEventMainThread(UpdatedListingEvent<BaseListingElement> event) {
        if (!CAPTURES_REQ.equals(event.getRequestId())) {
            return;
        }
        if (!mUserId.equals(event.getAccountId())) {
            return;
        }

        mFetching = false;

        if (mAdapter.getItems().isEmpty()) {
            mNoCapturesTextView.setVisibility(View.VISIBLE);
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
            return;
        }

        if (event.getListing() != null) {
            mCapturesListing = event.getListing();
            mAdapter.setItems(mCapturesListing.getUpdates());
            mAdapter.notifyDataSetChanged();
        }
        //if cacheListing is null, means there are no updates
        //we don't let mFollowerListing get assigned null
    }

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
        mEmptyStateHeader.setDataToView(mUserAccount);

        if (mUserAccount == null) {
            return;
        }
        boolean isSelf = mUserAccount.isUserRelationshipTypeSelf();
        String user = mUserAccount.getFname() != null ? mUserAccount.getFname() : "This user";
        String emptyText = isSelf
                ? getResources().getString(R.string.empty_own_profile)
                : String.format(getResources().getString(R.string.empty_user_profile), user);
        setEmptyStateText(emptyText);
    }

    @Override
    public void followerCountClicked() {
        Intent intent = FollowersFollowingActivity
                .newIntent(getActivity(), FollowersFollowingActivity.Type.FOLLOWERS, mUserId);
        startActivity(intent);
        //TODO convert back to fragments when viewpager fragment lifecycles is deciphered. when popping
        //the fragment back stack and going back to a screen with a viewpager, the view pager fragments disappear
        //launchNextFragment(FollowersFragment.newInstance(mUserId));
    }

    @Override
    public void followingCountClicked() {
        Intent intent = FollowersFollowingActivity
                .newIntent(getActivity(), FollowersFollowingActivity.Type.FOLLOWING, mUserId);
        startActivity(intent);
        //launchNextFragment(FollowingFragment.newInstance(mUserId));
    }

    private void loadLocalData() {

        new SafeAsyncTask<Listing<BaseListingElement>>(this) {
            @Override
            protected Listing<BaseListingElement> safeDoInBackground(Void[] params) {
                return mListingModel.getUserCaptures(mUserId);
            }

            @Override
            protected void safeOnPostExecute(Listing<BaseListingElement> listing) {

                if (listing != null) {
                    mCapturesListing = listing;
                    //items were successfully retrieved from cache, set to view!
                    mAdapter.setItems(listing.getUpdates());
                    mAdapter.notifyDataSetChanged();
                }

                mFetching = true;
                if (mAdapter.getItems().isEmpty()) {
                    //only if there were no cache items do we make the call to fetch entries
                    mAccountController.fetchAccountCaptures(CAPTURES_REQ, CapturesContext.DETAILS,
                            mUserId, null, false);

                } else {
                    //simulate a pull to refresh if there are items
                    mAccountController.fetchAccountCaptures(CAPTURES_REQ, CapturesContext.DETAILS,
                            mUserId, mCapturesListing, true);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void shouldLoadNextPage() {
        if (mFetching) {
            return;
        }

        if (mCapturesListing == null) {
            //reached end of list/there are no items, we do nothing.
            //though, this should never be null bc the fragment doesn't it allow it to be.
            return;
        }

        if (mCapturesListing.getMore()) {
            mFetching = true;
            //mNoFollowersText.setVisibility(View.GONE);
            mAccountController.fetchAccountCaptures(CAPTURES_REQ, CapturesContext.DETAILS,
                    mUserId, mCapturesListing, false);
        }
    }

    @Override
    public void reloadLocalData() {
        loadLocalData();
    }

    @Override
    public void dataSetChanged() {
        mAdapter.notifyDataSetChanged();
        if (mAdapter.isEmpty()) {
            mEmptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyStateLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void deleteCapture(CaptureDetails capture) {
        super.deleteCapture(capture);
        mAdapter.getItems().remove(capture);
        mAdapter.notifyDataSetChanged();
    }

    public void setEmptyStateText(String emptyText) {
        mEmptyStateText = emptyText;
        if (mNoCapturesTextView != null) {
            mNoCapturesTextView.setText(emptyText);
        }
    }

    @Override
    public void launchWineProfile(CaptureDetails captureDetails) {
        Intent intent = new Intent();
        // Launch WineProfile if the capture matched a wine, otherwise launch the capture details
        if (captureDetails.getWineProfile() != null) {
            intent = WineProfileActivity.newIntent(getActivity(), captureDetails.getWineProfile(),
                    captureDetails.getPhoto());
        } else {
            intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                    captureDetails.getId());
            intent.setClass(getActivity(), CaptureDetailsActivity.class);
        }
        startActivity(intent);
    }

    @Override
    public void launchWineProfile(PendingCapture capture) {

    }

    @Override
    public void addRatingAndComment(PendingCapture capture) {

    }

    @Override
    public void discardCapture(PendingCapture capture) {

    }
}
