package com.delectable.mobile.ui.capture.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.capture.fragment.TaggedPeopleFragment;

import android.os.Bundle;
import android.view.MenuItem;

public class TaggedPeopleActivity extends BaseActivity {

    private static final String TAG = TaggedPeopleActivity.class.getSimpleName();

    private String mCaptureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mCaptureId = args.getString(TaggedPeopleFragment.PARAMS_CAPTURE_ID);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TaggedPeopleFragment.newInstance(mCaptureId))
                    .commit();
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
