package com.delectable.mobile.ui.profile.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.AccountModel;
import com.delectable.mobile.api.cache.CapturesPendingCapturesListingModel;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.controllers.PendingCapturesController;
import com.delectable.mobile.api.endpointmodels.captures.CapturesContext;
import com.delectable.mobile.api.events.UpdatedListingEvent;
import com.delectable.mobile.api.events.accounts.FollowAccountEvent;
import com.delectable.mobile.api.events.accounts.UpdatedAccountProfileEvent;
import com.delectable.mobile.api.events.pendingcaptures.DeletedPendingCaptureEvent;
import com.delectable.mobile.api.models.AccountProfile;
import com.delectable.mobile.api.models.BaseListingElement;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureState;
import com.delectable.mobile.api.models.DeleteHash;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.PendingCapture;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.common.widget.InfiniteScrollAdapter;
import com.delectable.mobile.ui.common.widget.MutableForegroundColorSpan;
import com.delectable.mobile.ui.profile.activity.FollowersFollowingActivity;
import com.delectable.mobile.ui.profile.widget.CapturesPendingCapturesAdapter;
import com.delectable.mobile.ui.profile.widget.MinimalCaptureDetailRow;
import com.delectable.mobile.ui.profile.widget.MinimalPendingCaptureRow;
import com.delectable.mobile.ui.profile.widget.ProfileHeaderView;
import com.delectable.mobile.ui.wineprofile.activity.RateCaptureActivity;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;
import com.delectable.mobile.util.AnalyticsUtil;
import com.delectable.mobile.util.HideableActionBarScrollListener;
import com.delectable.mobile.util.MathUtil;
import com.delectable.mobile.util.SafeAsyncTask;
import com.melnykov.fab.FloatingActionButton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class UserProfileFragment extends BaseCaptureDetailsFragment implements
        ProfileHeaderView.ProfileHeaderActionListener, InfiniteScrollAdapter.ActionsHandler,
        MinimalPendingCaptureRow.ActionsHandler {

    private static final String TAG = UserProfileFragment.class.getSimpleName();

    protected static final String USER_ID = "USER_ID";

    private static final String CAPTURES_REQ = TAG + "_captures_req";

    private static final String DELETE_PENDING_CAPTURE_REQ = TAG + "_delete_pending_capture_req";

    private static final int DELETE_PENDING_CAPTURE_DIALOG = 1;

    private static final int ACTIONBAR_TRANSITION_ANIM_DURATION = 300;

    @Inject
    protected CapturesPendingCapturesListingModel mListingModel;

    @InjectView(R.id.list_view)
    protected ListView mListView;

    protected View mEmptyViewFooter;

    protected ContentLoadingProgressBar mProgressBar;

    protected FontTextView mNoCapturesTextView;

    @InjectView(R.id.camera_button)
    protected FloatingActionButton mCameraButton;

    @InjectView(R.id.toolbar)
    protected Toolbar mToolbar;

    private MutableForegroundColorSpan mAlphaSpan;

    private SpannableString mTitle;

    @Inject
    protected AccountController mAccountController;

    @Inject
    protected PendingCapturesController mPendingCapturesController;

    @Inject
    protected AccountModel mAccountModel;

    private CapturesPendingCapturesAdapter mAdapter;

    private Listing<BaseListingElement, DeleteHash> mCapturesListing;

    private boolean mFetching;

    private ProfileHeaderView mProfileHeaderView;

    private AccountProfile mUserAccount;

    private String mUserId;

    private PendingCapture mCaptureToDelete;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isFragmentEmbedded()) {
            enableBackButton(true);
            getActionBarToolbar().setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = View.inflate(getActivity(), R.layout.fragment_user_profile, null);
        ButterKnife.inject(this, view);

        mProfileHeaderView = (ProfileHeaderView) inflater
                .inflate(R.layout.profile_header_impl, mListView, false);
        mProfileHeaderView.setActionListener(this);

        mEmptyViewFooter = inflater.inflate(R.layout.row_empty_view, mListView, false);
        mProgressBar = (ContentLoadingProgressBar) mEmptyViewFooter.findViewById(R.id.progress_bar);
        mNoCapturesTextView = (FontTextView) mEmptyViewFooter
                .findViewById(R.id.nothing_to_display_textview);

        mListView.addHeaderView(mProfileHeaderView);
        mListView.addFooterView(mEmptyViewFooter);
        mListView.setOnScrollListener(new HideableActionBarScrollListener(this));
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                    long id) {
                if (view instanceof MinimalCaptureDetailRow) {
                    ((MinimalCaptureDetailRow) view).onOverflowClick();
                    return true;
                }
                return false;
            }
        });
        mListView.setAdapter(mAdapter);

        final HideableActionBarScrollListener hideableActionBarScrollListener
                = new HideableActionBarScrollListener(this);

        // Setup Floating Camera Button
        final FloatingActionButton.FabOnScrollListener fabOnScrollListener
                = new FloatingActionButton.FabOnScrollListener() {

//            boolean isTitleVisible = false;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                onScrollChanged();
                /*
                // decide if user name should be shown / transparent actionbar
                if (mListView != null && mListView.getChildCount() > 1) {

                    boolean firstItemVisible = mListView.getFirstVisiblePosition() > 0;
                    if (isTitleVisible != firstItemVisible) {
                        // title
                        ObjectAnimator titleAnimator = new ObjectAnimator()
                                .ofInt(mAlphaSpan, MutableForegroundColorSpan.ALPHA_PROPERTY,
                                        firstItemVisible ? 0 : 255, firstItemVisible ? 255 : 0);
                        titleAnimator.setDuration(ACTIONBAR_TRANSITION_ANIM_DURATION);
                        titleAnimator.setInterpolator(new DecelerateInterpolator());
                        titleAnimator.setEvaluator(new ArgbEvaluator());
                        titleAnimator
                                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                        setActionBarSubtitle(mTitle);
                                    }
                                });
                        titleAnimator.start();
                        // background
                        int solidColor = getResources().getColor(R.color.d_off_white);
                        int transparentColor = getResources()
                                .getColor(R.color.d_off_white_transparent);
                        final ValueAnimator bgAnimator = ValueAnimator.ofObject(
                                new ArgbEvaluator(),
                                firstItemVisible ? transparentColor : solidColor,
                                firstItemVisible ? solidColor : transparentColor);
                        bgAnimator.setDuration(ACTIONBAR_TRANSITION_ANIM_DURATION);
                        bgAnimator.setInterpolator(new DecelerateInterpolator());
                        bgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                getActionBarToolbar().setBackgroundColor(
                                        (Integer) bgAnimator.getAnimatedValue());
                            }
                        });
                        bgAnimator.start();
                    }
                    isTitleVisible = firstItemVisible;
                }

                hideableActionBarScrollListener
                        .onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

                */
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

    protected void onScrollChanged() {
        if (isFragmentEmbedded()) {
            return;
        }

        View v = mListView.getChildAt(0);
        int top = (v == null ? 0 : v.getTop());
        int headerHeight = mProfileHeaderView.getHeight();
        boolean isHeaderVisible = mListView.getFirstVisiblePosition() == 0;

        if (isHeaderVisible) {
            // drag toolbar off the screen when reaching the bottom of the header
            int toolbarHeight = mToolbar.getHeight();
            int toolbarDragOffset = 0;
            int toolbarTranslation = MathUtil.clamp(top + toolbarDragOffset, -toolbarHeight, 0);
            mToolbar.setTranslationY(toolbarTranslation);
        }
    }

    protected boolean isFragmentEmbedded() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();

        // fetch profile to check for updates (we're using eTags, so no big deal)
        mAccountController.fetchProfile(mUserId);

        boolean isOwnProfile = UserInfo.isLoggedInUser(getActivity(), mUserId);
        mAnalytics.trackViewUserProfile(
                isOwnProfile ? AnalyticsUtil.USER_PROFILE_OWN : AnalyticsUtil.USER_PROFILE_OTHERS);

        if (!mAdapter.isEmpty()) {
            mListView.removeFooterView(mEmptyViewFooter);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.profile_menu, menu);
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

    private void loadUserData() {

        new SafeAsyncTask<AccountProfile>(this) {
            @Override
            protected AccountProfile safeDoInBackground(Void[] params) {
                return mAccountModel.getAccount(mUserId);
            }

            @Override
            protected void safeOnPostExecute(AccountProfile account) {
                if (account != null) {
                    mUserAccount = account;
                    //items were successfully retrieved from cache, set to view!
                    updateViews(mUserAccount);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        loadLocalCapturesData();
    }

    @OnItemClick(R.id.list_view)
    protected void onItemClick(int position) {
        position--; //offset for header
        BaseListingElement item = mAdapter.getItem(position);
        if (item instanceof CaptureDetails) {
            launchWineProfile((CaptureDetails) item);
            return;
        }
        if (item instanceof PendingCapture) {
            launchWineProfile((PendingCapture) item);
        }
    }

    public void onEventMainThread(UpdatedAccountProfileEvent event) {

        if (!mUserId.equals(event.getAccount().getId())) {
            return;
        }

        if (event.isSuccessful()) {
            mUserAccount = event.getAccount();
            updateViews(mUserAccount);
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
        loadUserData();
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
        } else if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }
    }

    public void onEventMainThread(UpdatedListingEvent<BaseListingElement, DeleteHash> event) {
        if (!CAPTURES_REQ.equals(event.getRequestId())) {
            return;
        }
        if (!mUserId.equals(event.getAccountId())) {
            return;
        }

        mFetching = false;
        mProgressBar.hide();

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

        if (mAdapter.isEmpty()) { // header and footer count for one each
            mNoCapturesTextView.setVisibility(View.VISIBLE);
        } else {
            mListView.removeHeaderView(mEmptyViewFooter);
        }

    }

    @Override
    public void toggleFollowUserClicked(boolean isFollowingSelected) {
        mAccountController.followAccount(mUserId, isFollowingSelected);
    }

    private void updateViews(AccountProfile account) {
        if (account == null) {
            return;
        }
        mProfileHeaderView.setDataToView(account);

        boolean isSelf = account.isUserRelationshipTypeSelf();
        String user = account.getFname() != null ? account.getFname() : "This user";
        String emptyText = isSelf
                ? getResources().getString(R.string.empty_own_profile)
                : String.format(getResources().getString(R.string.empty_user_profile), user);
        setEmptyStateText(emptyText);

        mAlphaSpan = new MutableForegroundColorSpan(0,
                getResources().getColor(R.color.d_big_stone));
        mTitle = new SpannableString(account.getFullName());
//        mTitle = new SpannableString(mUserAccount.isUserRelationshipTypeSelf()
//                ? getString(R.string.you)
//                : mUserAccount.getFullName());
        mTitle.setSpan(mAlphaSpan, 0, mTitle.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        setActionBarSubtitle(mTitle);
    }

    @Override
    public void wineCountClicked() {
        // scroll down to wine list
        mListView.smoothScrollToPositionFromTop(1, 0, 400);
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

    private void loadLocalCapturesData() {

        new SafeAsyncTask<Listing<BaseListingElement, DeleteHash>>(this) {

            @Override
            protected void onPreExecute() {
                mProgressBar.show();
            }

            @Override
            protected Listing<BaseListingElement, DeleteHash> safeDoInBackground(Void[] params) {
                return mListingModel.getUserCaptures(mUserId);
            }

            @Override
            protected void safeOnPostExecute(Listing<BaseListingElement, DeleteHash> listing) {

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
        loadLocalCapturesData();
    }

    @Override
    public void dataSetChanged() {
        mAdapter.notifyDataSetChanged();
        mNoCapturesTextView.setVisibility(mAdapter.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void deleteCapture(CaptureDetails capture) {
        super.deleteCapture(capture);
        mAdapter.getItems().remove(capture);
        mAdapter.notifyDataSetChanged();
    }

    public void setEmptyStateText(String emptyText) {
        if (mNoCapturesTextView != null) {
            mNoCapturesTextView.setText(emptyText);
        }
    }

    @Override
    public void launchWineProfile(CaptureDetails captureDetails) {
        Intent intent = new Intent();

        // Launch WineProfile if the capture matched a wine, otherwise launch the capture details
        CaptureState state = CaptureState.getState(captureDetails);
        switch (state) {
            case UNVERIFIED:
                intent = WineProfileActivity.newIntent(getActivity(), captureDetails.getBaseWine(),
                        captureDetails.getPhoto());
                break;
            case IDENTIFIED:
                intent = WineProfileActivity
                        .newIntent(getActivity(), captureDetails.getWineProfile(),
                                captureDetails.getPhoto());
                break;
            case UNIDENTIFIED:
            case IMPOSSIBLED:
            default:
                intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                        captureDetails.getId());
                intent.setClass(getActivity(), CaptureDetailsActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void launchWineProfile(PendingCapture capture) {
        //TODO captureDetails/PendingCapture should implement parceable, then all this logic can be abstracted into WineProfileFragment
        Intent intent = null;
        CaptureState state = CaptureState.getState(capture);
        if (state == CaptureState.IDENTIFIED) {
            intent = WineProfileActivity.newIntent(getActivity(), capture.getWineProfile(),
                    capture.getPhoto());
        } else if (state == CaptureState.UNVERIFIED) {
            intent = WineProfileActivity.newIntent(getActivity(), capture.getBaseWine(),
                    capture.getPhoto());
        }
        //if the capture state is impossibled or unidentified, not enough data to launch into launch into wineprofile screen
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void addRatingAndComment(PendingCapture capture) {
        Intent intent = RateCaptureActivity.newIntent(getActivity(), capture.getId());
        startActivity(intent);
    }

    @Override
    public void discardCapture(PendingCapture capture) {
        mCaptureToDelete = capture;
        String message = getString(R.string.remove_this_wine_from_your_list);
        String remove = getString(R.string.remove);
        String cancel = getString(R.string.cancel);
        showConfirmationNoTitle(message, remove, cancel, DELETE_PENDING_CAPTURE_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_PENDING_CAPTURE_DIALOG) {
            if (resultCode == Activity.RESULT_OK) {
                mPendingCapturesController
                        .deleteCapture(DELETE_PENDING_CAPTURE_REQ, mUserId,
                                mCaptureToDelete.getId());
            }
            mCaptureToDelete = null;
        }
    }


    public void onEventMainThread(DeletedPendingCaptureEvent event) {
        if (!DELETE_PENDING_CAPTURE_REQ.equals(event.getRequestId())) {
            return;
        }

        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());

            //revert back to original array to show all items
            mAdapter.setType(CapturesPendingCapturesAdapter.Type.ALL);
            mAdapter.notifyDataSetChanged();
            return;
        }

        if (event.getState() == DeletedPendingCaptureEvent.State.DELETING) {
            //have the adapter hide the items that are in the "deleting" state
            mAdapter.setType(CapturesPendingCapturesAdapter.Type.WITHOUT_DELETING);
            mAdapter.notifyDataSetChanged();
            return;
        }

        if (event.getState() == DeletedPendingCaptureEvent.State.DELETED) {
            mAdapter.removeItem(event.getCaptureId());
            mAdapter.setType(CapturesPendingCapturesAdapter.Type.ALL);
            mAdapter.notifyDataSetChanged();
            return;
        }

    }

}
