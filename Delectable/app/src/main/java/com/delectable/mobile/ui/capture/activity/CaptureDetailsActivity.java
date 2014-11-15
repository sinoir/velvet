package com.delectable.mobile.ui.capture.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.capture.fragment.CaptureDetailsFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class CaptureDetailsActivity extends BaseActivity {

    public static final String PARAMS_CAPTURE_ID = "PARAMS_CAPTURE_ID";

    private static final String TAG = CaptureDetailsActivity.class.getSimpleName();

    /**
     * see {@link CaptureDetailsFragment#newInstance(String)}
     */
    public static Intent newIntent(Context packageContext, String captureId) {
        Intent intent = new Intent();
        intent.putExtra(PARAMS_CAPTURE_ID, captureId);
        intent.setClass(packageContext, CaptureDetailsActivity.class);
        return intent;
    }

    private String mCaptureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mCaptureId = args.getString(PARAMS_CAPTURE_ID);
        } else {
            // Check if Deep Link params contains data if the bundle args doesn't
            mCaptureId = getDeepLinkParam("capture_id");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, CaptureDetailsFragment.newInstance(mCaptureId))
                    .commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishDeepLinkActivity();
                break;
        }
        return true;
    }
}
