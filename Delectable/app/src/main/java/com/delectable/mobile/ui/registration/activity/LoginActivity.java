package com.delectable.mobile.ui.registration.activity;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.BaseActivity;
import com.delectable.mobile.ui.registration.fragment.LandingPageFragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LandingPageFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
