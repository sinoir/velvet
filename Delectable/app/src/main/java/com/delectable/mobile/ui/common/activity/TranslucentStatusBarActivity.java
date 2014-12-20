package com.delectable.mobile.ui.common.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;

import android.os.Bundle;

public class TranslucentStatusBarActivity extends BaseActivity {

    public static final String TAG = TranslucentStatusBarActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container_translucent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showOrHideStatusBar(true);
    }

}
