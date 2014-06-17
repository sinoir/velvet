package com.delectable.mobile.ui;

import com.delectable.mobile.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public abstract class BaseActivity extends Activity {

    public void replaceWithFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // TODO: Should we animate?
        transaction.setCustomAnimations(
                android.R.animator.fade_in, android.R.animator.fade_out,
                android.R.animator.fade_in, android.R.animator.fade_out);

        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}
