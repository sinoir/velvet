package com.delectable.mobile;

import com.kahuna.sdk.KahunaAnalytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = PushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // TODO: Fix / implmeent DEEP LINKS bit properly once Push Notif Creds are available.
        // Copied from GetStarted Guide
        if (action.equals(KahunaAnalytics.ACTION_PUSH_CLICKED)) {
            /* Push notification was clicked. */
            Bundle extras = intent.getBundleExtra(KahunaAnalytics.
                    EXTRA_LANDING_DICTIONARY_ID);
            if (extras != null) {
                // Make sure to check all values in case you receive pushes
                // without any parameters.
                String value = extras.getString("YOUR_PARAM_KEY");
                if (value != null) {
                    Log.i(TAG, "Received Kahuna push with your value:" + value);
                }
            }
        }
    }
}
