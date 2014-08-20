package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.api.RequestError;
import com.delectable.mobile.api.controllers.BaseNetworkController;
import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.requests.EditCommentRequest;
import com.delectable.mobile.api.requests.LikeCaptureActionRequest;
import com.delectable.mobile.api.requests.RateCaptureRequest;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.common.dialog.CommentAndRateDialog;
import com.delectable.mobile.ui.common.dialog.CommentDialog;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

public abstract class BaseCaptureDetailsFragment extends BaseFragment
        implements CaptureDetailsView.CaptureActionsHandler {

    private static final String TAG = BaseCaptureDetailsFragment.class.getSimpleName();

    @Inject
    CaptureController mCaptureController;

    private BaseNetworkController mNetworkController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNetworkController = new BaseNetworkController(getActivity());
    }

    public abstract void dataSetChanged();

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
                            dataSetChanged();
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
        dataSetChanged();
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
                dataSetChanged();
            }
        });
    }

    private void sendComment(final CaptureDetails capture, String comment) {
        // TODO: Loader?
        // Temp comment for instant UI
        final CaptureComment tempComment = new CaptureComment();
        tempComment.setAccountId(UserInfo.getUserId(getActivity()));
        tempComment.setComment(comment);
        if (capture.getComments() == null) {
            capture.setComments(new ArrayList<CaptureComment>());
        }
        capture.getComments().add(tempComment);
        dataSetChanged();

        mCaptureController.addCommentToCapture(capture.getId(), tempComment);
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
    public void launchWineProfile(CaptureDetails capture) {
        Intent intent = new Intent();
        // Don't launch the Wine Capture profile if the Wine is null, such as when the capture hasn't matched a Wine yet
        if (capture.getWineProfile() != null) {
            intent.putExtra(WineProfileActivity.PARAMS_WINE_PROFILE, capture.getWineProfile());
            intent.putExtra(WineProfileActivity.PARAMS_CAPTURE_PHOTO_HASH, capture.getPhoto());
            intent.setClass(getActivity(), WineProfileActivity.class);
            startActivity(intent);
        }
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
    public void discardCapture(CaptureDetails capture) {
        // TODO: Discard / Delete Capture
    }

    @Override
    public void editCapture(CaptureDetails capture) {
        // Not sure if this is what the edit icon does?
        rateAndCommentForCapture(capture);
    }

    private void sendRating(final CaptureDetails capture, final int rating) {
        final String userId = UserInfo.getUserId(getActivity());
        final int oldRating = capture.getRatingForId(userId);

        RateCaptureRequest request = new RateCaptureRequest(capture, rating);
        capture.updateRatingForUser(UserInfo.getUserId(getActivity()), rating);
        dataSetChanged();
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
                dataSetChanged();
            }
        });
    }

    public void onEventMainThread(AddCaptureCommentEvent event) {
        dataSetChanged();
    }
}
