package com.delectable.mobile;

import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.registration.activity.LoginActivity;
import com.delectable.mobile.util.DeepLink;
import com.delectable.mobile.util.KahunaUtil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String DELECTABLE_DEEPLINK_SCHEME = "delectable";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KahunaUtil.trackStart();
        App.injectMembers(this);

        Intent intent = getIntent();
        String action = intent.getAction();

        //spawned from deep link
        //the scheme/host pairs specified in the manifest are the only ones that can invoke this part of the code
        if (Intent.ACTION_VIEW.equals(action)) {
            if (intent.getData() != null) {
                Uri data = intent.getData();
                String scheme = data.getScheme();
                if (DELECTABLE_DEEPLINK_SCHEME.equalsIgnoreCase(scheme)) {
                    launchDeepLinkFlow(data);
                    return;
                }
            }
        }
        launchNavOrLogin();
    }

    private void launchNavOrLogin() {
        Intent launchIntent = new Intent();
        if (UserInfo.isSignedIn(this)) {
            launchIntent.setClass(getApplicationContext(), NavActivity.class);
        } else {
            launchIntent.setClass(getApplicationContext(), LoginActivity.class);
        }
        // Prevent multiples of the same Activity to be launched when clicking Push notification
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(launchIntent);

        finish();
    }

    /**
     * @return {@code true} if the event was consumed, {@code false} if it didn't.
     */
    private void launchDeepLinkFlow(Uri data) {
        //user needs to be signed in to access any part of the app
        if (!UserInfo.isSignedIn(this)) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Intent intent = DeepLink.getIntent(this, data);
        if (intent == null) {
            //catch undocumented deeplinks and redirect them to a normal app launch flow
            launchNavOrLogin();
            return;
        }

        //Synthesize backstack so when use hits back they go back to navactivity
        PendingIntent pendingIntent = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send(this, 0, new Intent());
            finish();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            launchNavOrLogin();
        }
    }
}


