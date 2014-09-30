package com.delectable.mobile;

import com.kahuna.sdk.KahunaAnalytics;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {

    private static final String TAG = PushReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle kahunaExtras = intent
                .getBundleExtra(KahunaAnalytics.EXTRA_LANDING_DICTIONARY_ID);
        Log.d(TAG, "Kahuna URL? : " + kahunaExtras.getString("url"));

        // TODO: Fix / implmeent DEEP LINKS bit properly once Push Notif Creds are available.
            /* Push notification was clicked. */
        if (action.equals(KahunaAnalytics.ACTION_PUSH_CLICKED) && kahunaExtras != null) {
            // Make sure to check all values in case you receive pushes
            // without any parameters.
            String deepLinkUrl = kahunaExtras.getString("url");
            if (deepLinkUrl != null) {
                openDeepLink(deepLinkUrl, context);
            }
        }
    }

    public void openDeepLink(String url, Context context) {
        Uri deepLinkUri = Uri.parse(url);
        try {
            Intent intent = new Intent();
            // TODO: Make the task in front, it goes behind the current task when app is already opened.
            // TODO: This Activity might be launched before MainActivity, try passing the data up to MainActivity for MainActivity to launch
            intent.setData(deepLinkUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // TODO: Add remote log here, this will happen if we have a new deep link url and we haven't implemented it yet...
            Log.wtf(TAG, "Failed to open deeplink", ex);
        }
    }
}