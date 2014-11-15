package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.api.events.captures.UpdatedCaptureDetailsEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.util.SafeAsyncTask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import javax.inject.Inject;

public class CaptureDetailsFragment extends BaseCaptureDetailsFragment {

    private static final String TAG = CaptureDetailsFragment.class.getSimpleName();

    private static final String sArgsCaptureId = "sArgsCaptureId";

    @Inject
    CaptureDetailsModel mCaptureDetailsModel;

    private View mScrollView;

    private CaptureDetailsView mCaptureDetailsView;

    private String mCaptureId;

    private CaptureDetails mCaptureDetails;

    public CaptureDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Fetches the capture for the captureId provided.
     */
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capture_details, container, false);
        mCaptureDetailsView = (CaptureDetailsView) view.findViewById(R.id.capture_details_view);
        mCaptureDetailsView.setActionsHandler(this);

        mScrollView = (ScrollView) view.findViewById(R.id.capture_details_scroll_view);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(
                new ViewTreeObserver.OnScrollChangedListener() {

                    View wineBanner = mCaptureDetailsView.findViewById(R.id.wine_banner);

                    View wineImageView = wineBanner.findViewById(R.id.wine_image);

                    @Override
                    public void onScrollChanged() {
                        int scrollY = mScrollView.getScrollY();
                        float height = wineBanner.getHeight();
                        // check if header is still visible
                        if (scrollY > height) {
                            return;
                        }
                        wineImageView.setTranslationY(scrollY / 2f);
                    }
                });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLocalData();
        mCaptureController.fetchCapture(mCaptureId);
    }

    @Override
    public void reloadLocalData() {
        loadLocalData();
    }

    @Override
    public void dataSetChanged() {
        mCaptureDetailsView.updateData(mCaptureDetails, true);
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
                    mCaptureDetailsView.updateData(mCaptureDetails, true);
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

    public void onEventMainThread(AddCaptureCommentEvent event) {
        if (event.isSuccessful() && mCaptureId.equals(event.getCaptureId())) {
            loadLocalData();
        }
    }
}
