package com.delectable.mobile.ui.capture.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.events.captures.AddCaptureCommentEvent;
import com.delectable.mobile.api.events.captures.UpdatedCaptureDetailsEvent;
import com.delectable.mobile.api.events.ui.InsetsChangedEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.ui.capture.widget.CaptureDetailsView;
import com.delectable.mobile.util.MathUtil;
import com.delectable.mobile.util.SafeAsyncTask;
import com.delectable.mobile.util.ScrimUtil;
import com.delectable.mobile.util.ViewUtil;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private View mWineBanner;

    private View mWineImageView;

    private View mStatusBarScrim;

    private Toolbar mToolbar;

    private int mStickyToolbarHeight;

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
        setHasOptionsMenu(true);

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

                    @Override
                    public void onScrollChanged() {
                        CaptureDetailsFragment.this.onScrollChanged();
                    }
                });

        mWineBanner = mCaptureDetailsView.findViewById(R.id.wine_banner);
        mWineImageView = mWineBanner.findViewById(R.id.wine_image);

        // set toolbar height to half the banner height for scroll offset
        Point screenSize = ViewUtil.getDisplayDimensions();
        mStickyToolbarHeight = screenSize.x / 2;

        mStatusBarScrim = view.findViewById(R.id.statusbar_scrim);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mStatusBarScrim.setBackground(ScrimUtil.STATUS_BAR_SCRIM);
        } else {
            mStatusBarScrim.setBackgroundDrawable(ScrimUtil.STATUS_BAR_SCRIM);
        }

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        getBaseActivity().setSupportActionBar(mToolbar);
        getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    private void onApplyWindowInsets(Rect insets) {
        if (insets == null) {
            return;
        }
        // adjust toolbar padding when status bar is translucent
        mToolbar.setPadding(0, insets.top, 0, 0);
//        mToolbarContrast.setPadding(0, insets.top, 0, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // increase scrim height when status bar is translucent to compensate for additional padding
            mStatusBarScrim.setMinimumHeight(mStatusBarScrim.getHeight() + insets.top);
        }
    }

    private void onScrollChanged() {
        int top = -mScrollView.getScrollY();
        int bannerHeight = mWineBanner.getHeight();

        // parallax effect on wine image
        if (top <= bannerHeight) {
            mWineImageView.setTranslationY(-top / 2f);
        }

        // drag toolbar off the screen when reaching the bottom of the header
        int toolbarHeight = mToolbar.getHeight();
        int toolbarDragOffset = bannerHeight - mStickyToolbarHeight;
        int toolbarTranslation = MathUtil.clamp(top + toolbarDragOffset, -toolbarHeight, 0);
        mToolbar.setTranslationY(toolbarTranslation);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.capture_details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            shareCapture(mCaptureDetails);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        InsetsChangedEvent insetsEvent = mEventBus.getStickyEvent(InsetsChangedEvent.class);
        if (insetsEvent != null) {
            onApplyWindowInsets(insetsEvent.insets);
        }

        loadLocalData();
        mCaptureController.fetchCapture(mCaptureId);

        mAnalytics.trackViewCaptureDetails();
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

    public void onEventMainThread(InsetsChangedEvent event) {
        onApplyWindowInsets(event.insets);
    }

    @Override
    public void deleteCapture(CaptureDetails capture) {
        super.deleteCapture(capture);
        getActivity().finish();
    }
}
