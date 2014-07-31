package com.delectable.mobile.ui.capture.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.capture.fragment.CaptureDetailsFragment;

import android.os.Bundle;
import android.view.MenuItem;

public class CaptureDetailsActivity extends BaseActivity {

    public static final String PARAMS_CAPTURE_ID = "PARAMS_CAPTURE_ID";

    private static final String TAG = CaptureDetailsActivity.class.getSimpleName();

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
            getFragmentManager().beginTransaction()
                    .add(R.id.container, CaptureDetailsFragment.newInstance(mCaptureId))
                    .commit();
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
