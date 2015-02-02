package com.mienaikoe.wifimesh;


import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    protected EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onStart() {
        super.onStart();
        try {
            mEventBus.registerSticky(this);
        } catch (Throwable t) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
    }

    protected void setDisplayHomeAsUpEnabled(boolean b) {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(b);
    }
}
