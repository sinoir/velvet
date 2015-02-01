package com.mienaikoe.wifimesh;

import android.support.v4.app.Fragment;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment{

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
}
