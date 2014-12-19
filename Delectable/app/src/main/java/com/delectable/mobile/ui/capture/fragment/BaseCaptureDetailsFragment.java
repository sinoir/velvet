package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.CaptureController;
import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.api.events.captures.DeletedCaptureEvent;
import com.delectable.mobile.api.events.captures.EditedCaptureCommentEvent;
import com.delectable.mobile.api.events.captures.LikedCaptureEvent;
import com.delectable.mobile.api.events.captures.RatedCaptureEvent;
import com.delectable.mobile.api.models.CaptureComment;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureState;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.activity.CaptureCommentRateActivity;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.activity.LikingPeopleActivity;
import com.delectable.mobile.ui.capture.activity.TaggedPeopleActivity;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;
import com.delectable.mobile.ui.wineprofile.dialog.Over21Dialog;
import com.delectable.mobile.ui.winepurchase.activity.WineCheckoutActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

public abstract class BaseCaptureDetailsFragment extends BaseFragment
        implements CaptureDetailsView.CaptureActionsHandler {

    private static final String TAG = BaseCaptureDetailsFragment.class.getSimpleName();

    private static final int REQUEST_DELETE_CONFIRMATION = 100;

    private static final int REQUEST_RATE_COMMENT_CAPTURE = 200;

    private static final int REQUEST_COMMENT_CAPTURE = 300;

    private static final int REQUEST_FLAG_CONFIRMATION = 400;

    private static final int REQUEST_AGE_DIALOG = 2;

    private String mToBePurchasedWineId = null;

    @Inject
    protected CaptureController mCaptureController;

    /**
     * Capture ready to be deleted or other things, for when the user either clicks OK.
     */
    private CaptureDetails mTempCaptureForAction;

    private CaptureComment mTempUserComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void reloadLocalData();

    public abstract void dataSetChanged();

    @Override
    public void writeCommentForCapture(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        Intent intent = new Intent();
        intent.putExtra(CaptureCommentRateActivity.PARAMS_IS_RATING, false);
        intent.setClass(getActivity(), CaptureCommentRateActivity.class);
        startActivityForResult(intent, REQUEST_COMMENT_CAPTURE);
    }

    @Override
    public void rateAndCommentForCapture(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        String userId = UserInfo.getUserId(getActivity());
        boolean isCurrentUserCapture = capture.getCapturerParticipant().getId()
                .equalsIgnoreCase(userId);
        ArrayList<CaptureComment> comments = capture.getCommentsForUserId(userId);
        mTempUserComment = comments.size() > 0 ? comments.get(0) : null;
        String currentUserCommentText = "";

        if (mTempUserComment != null && isCurrentUserCapture) {
            currentUserCommentText = mTempUserComment.getComment();
        }
        int currentUserRating = capture.getRatingForId(userId);

        Intent intent = new Intent();
        intent.putExtra(CaptureCommentRateActivity.PARAMS_IS_RATING, true);
        intent.putExtra(CaptureCommentRateActivity.PARAMS_RATING, currentUserRating);
        intent.putExtra(CaptureCommentRateActivity.PARAMS_COMMENT, currentUserCommentText);

        intent.setClass(getActivity(), CaptureCommentRateActivity.class);
        startActivityForResult(intent, REQUEST_RATE_COMMENT_CAPTURE);
    }

    @Override
    public void toggleLikeForCapture(final CaptureDetails capture, boolean isLiked) {
        mCaptureController.toggleLikeCapture(capture.getId(), isLiked);
    }

    private void sendComment(final CaptureDetails capture, String comment) {
        // TODO: Loader?
        if (comment != null && comment.trim().isEmpty()) {
            return; //do nothing if commment was empty
        }
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
        // Don't launch the Wine Capture profile if the Wine is null, such as when the capture hasn't matched a Wine yet
        if (capture.getWineProfile() != null) {
            Intent intent = WineProfileActivity.newIntent(getActivity(), capture.getWineProfile(),
                    capture.getPhoto());
            startActivity(intent);
        }
    }
    @Override
    public void checkPrice(CaptureDetails capture, CaptureDetailsView view) {
        //allow subclass to override and catch event
    }

        @Override
    public void launchPurchaseFlow(CaptureDetails capture) {
        if (!UserInfo.isOver21()) {
            mToBePurchasedWineId = capture.getWineProfile().getId();
            Over21Dialog dialog = Over21Dialog.newInstance();
            dialog.setTargetFragment(this, REQUEST_AGE_DIALOG);
            dialog.show(getFragmentManager(), "dialog");
        } else {
            launchWineCheckout(capture.getWineProfile().getId());
        }
    }

    private void launchWineCheckout(String wineId) {
        Intent intent = WineCheckoutActivity.newIntent(getActivity(), wineId);
        startActivity(intent);
    }

    @Override
    public void launchCaptureDetails(CaptureDetails capture) {
        Intent intent = new Intent();
        intent.putExtra(CaptureDetailsActivity.PARAMS_CAPTURE_ID,
                capture.getId());
        intent.setClass(getActivity(), CaptureDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void launchUserProfile(String userAccountId) {
        Intent intent = new Intent();
        intent.putExtra(UserProfileActivity.PARAMS_USER_ID, userAccountId);
        intent.setClass(getActivity(), UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void launchTaggedUsersListing(String captureId) {
        Intent intent = new Intent(getActivity(), TaggedPeopleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(TaggedPeopleFragment.PARAMS_CAPTURE_ID, captureId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void launchLikingUsersListing(CaptureDetails capture) {
        Intent intent = new Intent(getActivity(), LikingPeopleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(LikingPeopleFragment.PARAMS_LIKING_PEOPLE,
                capture.getLikingParticipants());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void discardCaptureClicked(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        showConfirmationNoTitle(getString(R.string.remove_this_wine_from_your_list),
                getString(R.string.minimal_capture_remove),
                null, REQUEST_DELETE_CONFIRMATION);
    }

    public void deleteCapture(CaptureDetails capture) {
        mCaptureController.deleteCapture(capture.getId());
    }

    @Override
    public void editCapture(CaptureDetails capture) {
        // Not sure if this is what the edit icon does?
        rateAndCommentForCapture(capture);
    }

    @Override
    public void flagCapture(CaptureDetails capture) {
        mTempCaptureForAction = capture;
        showConfirmationNoTitle(getString(R.string.capture_report), getString(R.string.report),
                null, REQUEST_FLAG_CONFIRMATION);
    }

    @Override
    public void shareCapture(CaptureDetails capture) {
        //prepare vintage string
        String vintage = "";
        CaptureState state = CaptureState.getState(capture);
        if (CaptureState.IDENTIFIED == state) {
            vintage = capture.getWineProfile().getVintage() + " ";
        }
        //strip NV or -- if necessary
        if (vintage.trim().equals("NV") ||
                vintage.trim().equals("--")) {
            vintage = "";
        }

        String shareText = getResources().getString(R.string.cap_action_recommend_text,
                capture.getDisplayTitle() + " " + vintage
                        + capture.getDisplayDescription(),
                capture.getShortShareUrl());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);
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

        // Attach EventBus to handle events / fixes weird issues
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
            // no-op
        }

        if (requestCode == REQUEST_DELETE_CONFIRMATION) {
            if (resultCode == Activity.RESULT_OK) {
                deleteCapture(mTempCaptureForAction);
            }
            mTempCaptureForAction = null;
        }

        if (requestCode == REQUEST_FLAG_CONFIRMATION) {
            if (resultCode == Activity.RESULT_OK) {
                CaptureState captureState = CaptureState.getState(mTempCaptureForAction);
                if (captureState == CaptureState.IDENTIFIED
                        || captureState == CaptureState.IMPOSSIBLED) {
                    // do not flag when it has not been identified yet but leave the option as a placebo
                    mCaptureController.flagCapture(mTempCaptureForAction.getId());
                }
            }
            mTempCaptureForAction = null;
        }

        if (requestCode == REQUEST_COMMENT_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                String commentText = data.getStringExtra(CaptureCommentRateFragment.DATA_COMMENT);
                Log.i(TAG, "Request Data Comment Text: " + commentText);
                sendComment(mTempCaptureForAction, commentText);
            }
            mTempCaptureForAction = null;
        }

        if (requestCode == REQUEST_RATE_COMMENT_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                String commentText = data.getStringExtra(CaptureCommentRateFragment.DATA_COMMENT);
                int rating = data.getIntExtra(CaptureCommentRateFragment.DATA_RATING, -1);
                Log.i(TAG, "Request Data Comment Text: " + commentText);
                Log.i(TAG, "Request Data Rating: " + rating);
                sendRating(mTempCaptureForAction, rating);
                if (mTempUserComment != null && mTempUserComment.getId() != null) {
                    mTempUserComment.setComment(commentText);
                    editComment(mTempCaptureForAction, mTempUserComment);
                    dataSetChanged();
                } else {
                    sendComment(mTempCaptureForAction, commentText);
                }
            }
            mTempUserComment = null;
            mTempCaptureForAction = null;
        }

        if (requestCode == REQUEST_AGE_DIALOG && resultCode == Over21Dialog.RESULT_OVER21) {
            launchWineCheckout(mToBePurchasedWineId);
        }
    }

    //endregion

    //region EventBus Events
    public void onEventMainThread(AddCaptureCommentEvent event) {
        handleActionEvent(event);
    }

    public void onEventMainThread(EditedCaptureCommentEvent event) {
        handleActionEvent(event);
    }

    public void onEventMainThread(LikedCaptureEvent event) {
        handleActionEvent(event);
    }

    public void onEventMainThread(RatedCaptureEvent event) {
        handleActionEvent(event);
    }

    public void onEventMainThread(DeletedCaptureEvent event) {
        handleActionEvent(event);
    }
    //endregion

    private void handleActionEvent(BaseEvent event) {
        if (event.isSuccessful()) {
            dataSetChanged();
        } else {
            reloadLocalData();
        }
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(ErrorUtil.NO_NETWORK_ERROR.getUserFriendlyMessage());
        } else if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }
    }
}
