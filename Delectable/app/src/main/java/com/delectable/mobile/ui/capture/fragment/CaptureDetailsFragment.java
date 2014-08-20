package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.events.captures.UpdatedCaptureDetailsEvent;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.util.SafeAsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

public class CaptureDetailsFragment extends BaseCaptureDetailsFragment {

    private static final String TAG = CaptureDetailsFragment.class.getSimpleName();

    private static final String sArgsCaptureId = "sArgsCaptureId";

    @Inject
    CaptureController mCaptureController;

    @Inject
    CaptureDetailsModel mCaptureDetailsModel;

    private View mView;

    private CaptureDetailsView mCaptureDetailsView;

    private String mCaptureId;

    private CaptureDetails mCaptureDetails;

    public CaptureDetailsFragment() {
        // Required empty public constructor
    }

    public static CaptureDetailsFragment newInstance(String captureId) {
        CaptureDetailsFragment fragment = new CaptureDetailsFragment();
        Bundle args = new Bundle();
        args.putString(sArgsCaptureId, captureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

        Bundle args = getArguments();
        if (args != null) {
            mCaptureId = args.getString(sArgsCaptureId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_capture_details, container, false);
        mCaptureDetailsView = (CaptureDetailsView) mView.findViewById(R.id.capture_details_view);
        mCaptureDetailsView.setActionsHandler(this);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCaptureController.fetchCapture(mCaptureId);
        loadLocalData();
    }

    @Override
    public void dataSetChanged() {
        mCaptureDetailsView.updateData(mCaptureDetails);
    }

    private void loadLocalData() {
        new SafeAsyncTask<CaptureDetails>(this) {
            @Override
            protected CaptureDetails safeDoInBackground(Void[] params) {
                return mCaptureDetailsModel.getCapture(mCaptureId);
            }

            @Override
            protected void safeOnPostExecute(CaptureDetails capture) {
                mCaptureDetails = capture;
                Log.d(TAG, "Loaded Capture: " + mCaptureDetails);
                if (mCaptureDetails != null) {
                    mCaptureDetailsView.updateData(mCaptureDetails);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void onEventMainThread(UpdatedCaptureDetailsEvent event) {
        if (!mCaptureId.equals(event.getCaptureId())) {
            return;
        }
        if (event.isSuccessful()) {
            loadLocalData();
        } else if (event.getErrorMessage() != null) {
            showToastError(event.getErrorMessage());
        }
    }
}
