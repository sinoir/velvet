package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.events.captures.DeletedCaptureEvent;
import com.delectable.mobile.events.captures.EditedCaptureCommentEvent;
import com.delectable.mobile.events.captures.LikedCaptureEvent;
import com.delectable.mobile.events.captures.RatedCaptureEvent;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.common.dialog.CommentAndRateDialog;
import com.delectable.mobile.ui.common.dialog.CommentDialog;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

public abstract class BaseCaptureDetailsFragment extends BaseFragment
        implements CaptureDetailsView.CaptureActionsHandler {

    private static final String TAG = BaseCaptureDetailsFragment.class.getSimpleName();

    private static final int REQUEST_DELETE_CONFIRMATION = 100;

    @Inject
    CaptureController mCaptureController;

    /**
     * Capture ready to be deleted or other things, for when the user either clicks OK.
     */
    private CaptureDetails mTempCaptureForAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        if (firstUserComment != null && firstUserComment.getId() != null) {
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
        mCaptureController.toggleLikeCapture(capture.getId(), userId, userLikesCapture);
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

        mCaptureController.addCommentToCapture(capture.getId(), comment);
    }

    private void editComment(CaptureDetails capture, final CaptureComment captureComment) {
        mCaptureController.editCaptureComment(capture.getId(), captureComment.getId(),
                captureComment.getComment());
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
    public void discardCaptureClicked(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        showConfirmation(getActivity().getString(R.string.remove),
                getActivity().getString(R.string.capture_remove),
                getActivity().getString(R.string.remove), REQUEST_DELETE_CONFIRMATION);
    }

    public void deleteCapture(CaptureDetails capture) {
        mCaptureController.deleteCapture(capture.getId());
    }

    @Override
    public void editCapture(CaptureDetails capture) {
        // Not sure if this is what the edit icon does?
        rateAndCommentForCapture(capture);
    }

    private void sendRating(final CaptureDetails capture, final int rating) {
        String userId = UserInfo.getUserId(getActivity());
        // Instant UI update
        capture.updateRatingForUser(UserInfo.getUserId(getActivity()), rating);
        dataSetChanged();
        // update rated capture
        mCaptureController.rateCapture(capture.getId(), userId, rating);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DELETE_CONFIRMATION) {
            if (resultCode == Activity.RESULT_OK) {
                deleteCapture(mTempCaptureForAction);
            }
            mTempCaptureForAction = null;
        }
    }

    //endregion

    //region EventBus Events
    public void onEventMainThread(AddCaptureCommentEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(EditedCaptureCommentEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(LikedCaptureEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(RatedCaptureEvent event) {
        dataSetChanged();
    }

    public void onEventMainThread(DeletedCaptureEvent event) {
        dataSetChanged();
    }
    //endregion
}
