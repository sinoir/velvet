package com.delectable.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.delectable.mobile.api.controllers.VersionPropsFileController;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.events.builddatecheck.BuildDateCheckedEvent;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.registration.activity.LoginActivity;
import com.delectable.mobile.ui.versionupgrade.dialog.VersionUpgradeDialog;
import com.delectable.mobile.util.KahunaUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KahunaUtil.trackStart();
        App.injectMembers(this);
        setContentView(R.layout.activity_fragment_container);

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
}


