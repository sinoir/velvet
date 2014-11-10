package com.delectable.mobile.ui.wineprofile.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.wineprofile.fragment.RateCaptureFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class RateCaptureActivity extends BaseActivity {

    private static final String PENDING_CAPTURE_ID = "PENDING_CAPTURE_ID";

    private String mPendingCaptureId;

    public static Intent newIntent(Context packageContext, String pendingCaptureId) {
        Intent intent = new Intent();
        intent.putExtra(PENDING_CAPTURE_ID, pendingCaptureId);
        intent.setClass(packageContext, RateCaptureActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mPendingCaptureId = args.getString(PENDING_CAPTURE_ID);
        }

        if (savedInstanceState == null) {
            RateCaptureFragment fragment = RateCaptureFragment.newInstance(mPendingCaptureId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
