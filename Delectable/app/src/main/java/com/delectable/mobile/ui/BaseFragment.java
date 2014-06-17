package com.delectable.mobile.ui;

import android.app.Fragment;

public class BaseFragment extends Fragment {

    public void launchNextFragment(BaseFragment fragment) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.replaceWithFragment(fragment);
    }
}
