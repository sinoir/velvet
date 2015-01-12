package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.controllers.PendingCapturesController;
import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.events.pendingcaptures.SetBaseWineEvent;
import com.delectable.mobile.api.events.scanwinelabel.CreatedPendingCaptureEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.wineprofile.activity.RateCaptureActivity;
import com.delectable.mobile.ui.wineprofile.dialog.EditBaseWineDialog;
import com.delectable.mobile.util.Animate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

public class WineProfileInstantFragment extends WineProfileFragment {

    public static final String TAG = WineProfileInstantFragment.class.getSimpleName();

    protected static final String PENDING_CAPTURE_ID = "pendingCaptureId";

    private static final int EDIT_BASE_WINE_DIALOG = 400;

    @Inject
    public PendingCapturesController mPendingCapturesController;

    private View mActionView;

    private FontTextView mRateButton;

    private String mPendingCaptureId;

    private Bitmap mPreviewImage;

    private ArrayList<BaseWine> mMatches;

    private boolean mNeedToSetBaseWineId = false;

    public static WineProfileInstantFragment newInstance(@Nullable BaseWine baseWine) {
        WineProfileInstantFragment fragment = new WineProfileInstantFragment();
        Bundle args = new Bundle();
        if (baseWine != null) {
            args.putString(BASE_WINE_ID, baseWine.getId());
        }
        fragment.setArguments(args);
        return fragment;
    }

    public void init(ArrayList<BaseWine> matches) {
        init(matches, null);
    }

    public void init(ArrayList<BaseWine> matches, Bitmap previewImage) {
        getArguments().putString(BASE_WINE_ID, matches.get(0).getId());
        mFetchingId = mBaseWineId = matches.get(0).getId();
        mBaseWineMinimal = matches.get(0);
        mMatches = matches;
        mPreviewImage = previewImage;
        updateBannerView(previewImage);
        onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View mView = super.onCreateView(inflater, container, savedInstanceState);
        mActionView = inflater.inflate(R.layout.action_menu_button, null, false);
        mRateButton = (FontTextView) mActionView.findViewById(R.id.action_button);

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.capture_menu, menu);
        MenuItem postItem = menu.findItem(R.id.post);
        mRateButton.setText(getString(R.string.capture_rate).toLowerCase());
        mRateButton.setEnabled(true);
        mRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open rate & comment screen
                mEventBus.postSticky(new RateCaptureFragment.RateCaptureInitEvent(mPreviewImage));
                Intent intent = RateCaptureActivity.newIntent(getActivity(), mPendingCaptureId);
                startActivity(intent);
            }
        });
        // TODO always show rate button once offline capture is working
        mRateButton.setEnabled(false);
        mRateButton.setTranslationX(Animate.TRANSLATION);
        mRateButton.setAlpha(0);
        MenuItemCompat.setActionView(postItem, mActionView);
    }

    public void onEventMainThread(CreatedPendingCaptureEvent event) {
        if (event.isSuccessful()) {
            if (event.getPendingCapture() != null) {
                mPendingCaptureId = event.getPendingCapture().getId();
                getArguments().putString(PENDING_CAPTURE_ID, mPendingCaptureId);
                mRateButton.setEnabled(true);
                Animate.pushInLeft(mRateButton);

                if (mNeedToSetBaseWineId) {
                    setBaseWine(mBaseWineMinimal);
                }
            }
        } else {
            handleEventErrorMessage(event);
        }
    }

    public void onEventMainThread(SetBaseWineEvent event) {
        if (event.isSuccessful() && mPendingCaptureId.equals(event.getPendingCaptureId())) {
            Log.d(TAG,
                    "changed base_wine_id for pending capture " + mPendingCaptureId + " to " + event
                            .getBaseWineId());
        } else {
            handleEventErrorMessage(event);
        }
    }

    private void handleEventErrorMessage(BaseEvent event) {
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(R.string.error_capture_no_network);
        } else {
            showToastError(event.getErrorMessage());
        }
        mRateButton.setEnabled(false);
//        Animate.pushOutRight(mRateButton);
    }

    @Override
    public void onEditBaseWineClicked() {
        showEditBaseWineDialog();
    }

    @Override
    protected void onCameraButtonClicked() {
        launchWineCapture();
        // finish activity to prevent infinite back stack
        getActivity().finish();
    }

    private void showEditBaseWineDialog() {
        EditBaseWineDialog dialog = EditBaseWineDialog.newInstance(mMatches);
        dialog.setTargetFragment(this, EDIT_BASE_WINE_DIALOG); //callback goes to onActivityResult
        dialog.show(getFragmentManager(), EditBaseWineDialog.TAG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (EDIT_BASE_WINE_DIALOG == requestCode && resultCode == Activity.RESULT_OK) {
            // TODO get data from new base wine and update pending capture id locally and on server
            if (data.getExtras() != null) {
                Parcelable p = data.getExtras()
                        .getParcelable(EditBaseWineDialog.EXTRAS_RESULT_WINE);
                if (p == null) {
                    // clicked footer, placebo report incorrect wine
                    Toast.makeText(getActivity(), R.string.capture_edit_base_wine_report_confirm,
                            Toast.LENGTH_SHORT).show();
                } else {
                    BaseWine baseWine = (BaseWine) p;
                    setBaseWine(baseWine);
                }
            }
        }
    }

    private void setBaseWine(BaseWineMinimal baseWine) {
        // refresh wine profile
        mFetchingId = mBaseWineId = baseWine.getId();
        mBaseWineMinimal = baseWine;
        updateBannerView(mPreviewImage);
        loadLocalBaseWineData(true); //load from model to show something first
        mBaseWineController.fetchBaseWine(mBaseWineId);
        loadCaptureNotesData(Type.BASE_WINE, mBaseWineId);
        // sync with backend
        if (mPendingCaptureId != null) {
            mPendingCapturesController
                    .setBaseWineId(mPendingCaptureId, mBaseWineId);
            mNeedToSetBaseWineId = false;
            Log.d(TAG, "setting base_wine_id for pending capture " + mPendingCaptureId + " to"
                    + mBaseWineId);
        } else {
            mNeedToSetBaseWineId = true;
        }
    }
}
