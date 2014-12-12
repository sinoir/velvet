package com.delectable.mobile.ui.camera.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.camera.fragment.WineCaptureCameraFragment;

import android.os.Bundle;
import android.view.MenuItem;

public class WineCaptureActivity extends BaseActivity {

    public static final String TAG = WineCaptureActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_toolbar_fragment_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WineCaptureCameraFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                launchUserProfile(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
