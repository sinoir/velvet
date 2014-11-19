package com.delectable.mobile.ui.common.dialog;

import com.delectable.mobile.App;

import android.os.Bundle;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class BaseEventBusDialogFragment extends BaseDialogFragment {

    @Inject
    protected EventBus mEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
            // no-op
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
