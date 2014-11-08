package com.delectable.mobile.ui.camera.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.camera.fragment.WineCaptureCameraFragment;

import android.os.Bundle;

public class WineCaptureActivity extends BaseActivity {

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
}
