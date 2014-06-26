package com.delectable.mobile.ui.home.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.AccountsNetworkController;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureDetailsListing;
import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.api.requests.AccountsFollowerFeedRequest;
import com.delectable.mobile.api.requests.CommentCaptureRequest;
import com.delectable.mobile.api.requests.EditCommentRequest;
import com.delectable.mobile.api.requests.LikeCaptureActionRequest;
import com.delectable.mobile.api.requests.RateCaptureRequest;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.common.dialog.CommentAndRateDialog;
import com.delectable.mobile.ui.common.dialog.CommentDialog;
import com.delectable.mobile.ui.common.widget.FollowFeedAdapter;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FollowFeedTabFragment extends BaseFragment implements
        FollowFeedAdapter.FeedItemActionsHandler {

    private static final String TAG = "FollowFeedTabFragment";

    private View mView;

    private SwipeRefreshLayout mRefreshContainer;

    private ListView mListView;

    private FollowFeedAdapter mAdapter;

    private AccountsNetworkController mAccountsNetworkController;

    private CaptureDetailsListing mDetailsListing;

    private ArrayList<CaptureDetails> mCaptureDetails;

    private boolean mIsLoadingData;

    private BaseNetworkController mNetworkController;

    public FollowFeedTabFragment() {
        // Required empty public constructor
    }

    public static FollowFeedTabFragment newInstance() {
        FollowFeedTabFragment fragment = new FollowFeedTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaptureDetails = new ArrayList<CaptureDetails>();
        mAccountsNetworkController = new AccountsNetworkController(getActivity());
        mNetworkController = new BaseNetworkController(getActivity());
        mIsLoadingData = false;
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

        mAdapter = new FollowFeedAdapter(getActivity(), mCaptureDetails, this);
        mListView.setAdapter(mAdapter);

        setupPullToRefresh();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
        loadData(true);
    }

    private void loadNextPage() {
        Log.d(TAG, "Load Next Page? " + mDetailsListing.getMore());
        // TODO: Figure out why last page returns invalidate and doesn't load more pages, why is more true?
        if (mDetailsListing != null &&
                mDetailsListing.getMore() &&
                (!mDetailsListing.getInvalidate()
                        || mDetailsListing.getBoundariesFromAfter() == null)) {
            loadData(false);
        }
    }

    private void loadData(boolean isRefreshing) {
        Log.d(TAG, "Load Data isRefreshing: ? " + isRefreshing);
        if (!mIsLoadingData) {
            mIsLoadingData = true;
            AccountsFollowerFeedRequest request;
            if (mDetailsListing == null) {
                request = new AccountsFollowerFeedRequest(
                        AccountsFollowerFeedRequest.CONTEXT_DETAILS);
            } else {
                request = new AccountsFollowerFeedRequest(mDetailsListing, isRefreshing);
            }
            performRequest(request);
            mRefreshContainer.setRefreshing(true);
        }
    }

    private void performRequest(AccountsFollowerFeedRequest request) {
        Log.d(TAG, "Request: " + request);
        mAccountsNetworkController.performRequest(request,
                new BaseNetworkController.RequestCallback() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        Log.d(TAG, "Received Results! " + result);
                        mDetailsListing = (CaptureDetailsListing) result;
                        mRefreshContainer.setRefreshing(false);
                        mIsLoadingData = false;
                        updateDisplayData();
                    }

                    @Override
                    public void onFailed(RequestError error) {
                        mRefreshContainer.setRefreshing(false);
                        mIsLoadingData = false;
                        Log.d(TAG, "Results Failed! " + error.getMessage() + " Code:" + error
                                .getCode());
                        // TODO: What to do with errors?
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
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
    public void writeCommentForCapture(final CaptureDetails capture) {
        CommentDialog dialog = new CommentDialog(getActivity(),
                new CommentDialog.CommentDialogCallback() {
                    @Override
                    public void onFinishWritingComment(String comment) {
                        sendComment(capture, comment);
                    }
                }
        );
        dialog.show();
    }

    @Override
    public void rateAndCommentForCapture(final CaptureDetails capture) {
        String userId = UserInfo.getUserId(getActivity());
        boolean isCurrentUserCapture = capture.getCapturerParticipant().getId()
                .equalsIgnoreCase(userId);
        ArrayList<CaptureComment> comments = capture.getCommentsForUserId(userId);
        final CaptureComment firstUserComment = comments.size() > 0 ? comments.get(0) : null;
        String currentUserCommentText = "";

        if (firstUserComment != null && isCurrentUserCapture) {
            currentUserCommentText = firstUserComment.getComment();
        }
        int currentUserRating = capture.getRatingForId(userId);
        CommentAndRateDialog dialog = new CommentAndRateDialog(getActivity(),
                currentUserCommentText, currentUserRating,
                new CommentAndRateDialog.CommentAndRateDialogCallback() {
                    @Override
                    public void onFinishWritingCommentAndRating(String comment, int rating) {
                        sendRating(capture, rating);
                        if (firstUserComment != null) {
                            firstUserComment.setComment(comment);
                            editComment(capture, firstUserComment);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            sendComment(capture, comment);
                        }
                    }
                }
        );
        dialog.show();
    }

    @Override
    public void toggleLikeForCapture(final CaptureDetails capture) {
        final String userId = UserInfo.getUserId(getActivity());
        boolean userLikesCapture = !capture.doesUserLikeCapture(userId);
        capture.toggleUserLikesCapture(userId);
        mAdapter.notifyDataSetChanged();
        LikeCaptureActionRequest likeRequest = new LikeCaptureActionRequest(capture,
                userLikesCapture);
        mNetworkController.performRequest(likeRequest, new BaseNetworkController.RequestCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                // Success
            }

            @Override
            public void onFailed(RequestError error) {
                Toast.makeText(getActivity(), "Failed to like capture", Toast.LENGTH_SHORT).show();
                // Reset like
                capture.toggleUserLikesCapture(userId);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sendRating(final CaptureDetails capture, final int rating) {
        final String userId = UserInfo.getUserId(getActivity());
        final int oldRating = capture.getRatingForId(userId);

        RateCaptureRequest request = new RateCaptureRequest(capture, rating);
        capture.updateRatingForUser(UserInfo.getUserId(getActivity()), rating);
        mAdapter.notifyDataSetChanged();
        mNetworkController.performRequest(request, new BaseNetworkController.RequestCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                // Success
            }

            @Override
            public void onFailed(RequestError error) {
                Toast.makeText(getActivity(), "Failed to rate capture", Toast.LENGTH_SHORT).show();
                // Reset displayed rating
                capture.updateRatingForUser(UserInfo.getUserId(getActivity()), oldRating);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void sendComment(final CaptureDetails capture, String comment) {
        CommentCaptureRequest request = new CommentCaptureRequest(capture, comment);
        // TODO: Loader?
        // Temp comment for seamless UI
        final CaptureComment tempComment = new CaptureComment();
        tempComment.setAccountId(UserInfo.getUserId(getActivity()));
        tempComment.setComment(comment);
        if (capture.getComments() == null) {
            capture.setComments(new ArrayList<CaptureComment>());
        }
        capture.getComments().add(tempComment);
        mAdapter.notifyDataSetChanged();
        mNetworkController.performRequest(request, new BaseNetworkController.RequestCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                // Merge new capture with old capture to retain sorting of the mCaptureDetails list
                CaptureDetails newCapture = (CaptureDetails) result;
                capture.updateWithNewCapture(newCapture);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(RequestError error) {
                Toast.makeText(getActivity(), "Failed to comment capture", Toast.LENGTH_SHORT)
                        .show();
                capture.getComments().remove(tempComment);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void editComment(CaptureDetails capture, final CaptureComment captureComment) {
        EditCommentRequest request = new EditCommentRequest(capture, captureComment);
        // TODO: Loader?
        mNetworkController.performRequest(request, new BaseNetworkController.RequestCallback() {
            @Override
            public void onSuccess(BaseResponse result) {
                // Success
            }

            @Override
            public void onFailed(RequestError error) {
                Toast.makeText(getActivity(), "Failed to comment capture", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void launchWineProfile(WineProfile wineProfile) {
        // TODO: Wine Profile
        Toast.makeText(getActivity(), "Wine Profile: " + wineProfile.getName(), Toast.LENGTH_SHORT)
                .show();
        Log.d(TAG,
                "Launch Wine Profile: " + wineProfile.getId() + " Name: " + wineProfile.getName());
    }

    @Override
    public void launchUserProfile(String userAccountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, userAccountId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void launchTaggedUserListing(CaptureDetails capture) {
        // TODO: Tagged User Listing
        Toast.makeText(getActivity(), "All Tagged Users list", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Launch Extra Tagged User Listing Screen.");
    }

    @Override
    public void shouldLoadNextPage() {
        loadNextPage();
    }
}
