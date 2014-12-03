package com.delectable.mobile.ui.wineprofile.fragment;

import com.delectable.mobile.R;
import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.events.scanwinelabel.CreatedPendingCaptureEvent;
import com.delectable.mobile.api.models.BaseWine;
import com.delectable.mobile.api.models.BaseWineMinimal;
import com.delectable.mobile.api.util.ErrorUtil;
import com.delectable.mobile.ui.common.widget.FontTextView;
import com.delectable.mobile.ui.wineprofile.activity.RateCaptureActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class WineProfileInstantFragment extends WineProfileFragment {

    public static final String TAG = WineProfileInstantFragment.class.getSimpleName();

    protected static final String PENDING_CAPTURE_ID = "pendingCaptureId";

    private View mActionView;

    private FontTextView mRateButton;

    private String mPendingCaptureId;

    public static WineProfileInstantFragment newInstance(@Nullable BaseWine baseWine) {
        WineProfileInstantFragment fragment = new WineProfileInstantFragment();
        Bundle args = new Bundle();
        if (baseWine != null) {
            args.putString(BASE_WINE_ID, baseWine.getId());
        }
        fragment.setArguments(args);
        return fragment;
    }

    public void init(BaseWineMinimal baseWine) {
        init(baseWine, null);
    }

    public void init(BaseWineMinimal baseWine, Bitmap previewImage) {
        getArguments().putString(BASE_WINE_ID, baseWine.getId());
        mFetchingId = mBaseWineId = baseWine.getId();
        mBaseWineMinimal = baseWine;
        updateBannerData(previewImage);
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
                Intent intent = RateCaptureActivity.newIntent(getActivity(), mPendingCaptureId);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // TODO
        mRateButton.setEnabled(false);
        MenuItemCompat.setActionView(postItem, mActionView);
    }

    public void onEventMainThread(CreatedPendingCaptureEvent event) {
        if (event.isSuccessful()) {
            if (event.getPendingCapture() != null) {
                mPendingCaptureId = event.getPendingCapture().getId();
                getArguments().putString(PENDING_CAPTURE_ID, mPendingCaptureId);
                mRateButton.setEnabled(true);
                Log.d(TAG, "%%%%%%%%%%%%%%%%%%%% received pendingCaptureId=" + mPendingCaptureId);
            }
        } else {
            handleEventErrorMessage(event);
        }
    }

    private void handleEventErrorMessage(BaseEvent event) {
        if (event.getErrorCode() == ErrorUtil.NO_NETWORK_ERROR) {
            showToastError(R.string.error_capture_wine_no_network);
        } else {
            showToastError(event.getErrorMessage());
        }
        mRateButton.setEnabled(false);
    }

}
