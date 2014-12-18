package com.delectable.mobile.ui.camera.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.camera.fragment.WineCaptureCameraFragment;
import com.delectable.mobile.ui.common.activity.TranslucentStatusBarActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class WineCaptureActivity extends TranslucentStatusBarActivity {

    public static final String TAG = WineCaptureActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            WineCaptureCameraFragment fragment = new WineCaptureCameraFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
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
