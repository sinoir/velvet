package com.delectable.mobile;

import com.crashlytics.android.Crashlytics;
import com.delectable.mobile.controllers.VersionPropsFileController;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.builddatecheck.BuildDateCheckedEvent;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.registration.activity.LoginActivity;
import com.delectable.mobile.ui.versionupgrade.dialog.VersionUpgradeDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    EventBus mEventBus;

    @Inject
    VersionPropsFileController mController;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mEventBus.register(this);
        } catch (Throwable t) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mEventBus.unregister(this);
        } catch (Throwable t) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(BuildConfig.REPORT_CRASHES) {
            Crashlytics.start(this);
        }
        App.injectMembers(this);
        setContentView(R.layout.activity_fragment_container);

        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("alpha")) {
            mController.checkForNewBuild();
            return;
        }

        //launch normally if build type is not alpha
        launchNavOrLogin();

    }

    private void launchNavOrLogin() {
        Intent launchIntent = new Intent();
        if (UserInfo.isSignedIn(this)) {
            launchIntent.setClass(getApplicationContext(), NavActivity.class);
        } else {
            launchIntent.setClass(getApplicationContext(), LoginActivity.class);
        }
        startActivity(launchIntent);

        finish();
    }

    public void onEventMainThread(BuildDateCheckedEvent event) {
        if (!event.isSuccessful()) {
            Toast.makeText(this, event.getErrorMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        //event was successful
        if (event.shouldUpdate()) {
            VersionUpgradeDialog dialog = VersionUpgradeDialog.newInstance();
            dialog.show(getFragmentManager(), VersionUpgradeDialog.TAG);
            dialog.setActionsHandler(ActionsHandler);
        } else {
            launchNavOrLogin();
        }
    }

    private VersionUpgradeDialog.ActionsHandler ActionsHandler
            = new VersionUpgradeDialog.ActionsHandler() {
        @Override
        public void onCancelClick() {
            launchNavOrLogin();
        }

        @Override
        public void onUpgradeClick() {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getS3Url()));
            startActivity(browserIntent);
        }
    };

    private String getS3Url() {
        String url = null;
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("s3.properties");

            Properties properties = new Properties();
            properties.load(inputStream);
            url = properties.getProperty("S3_LINK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }


}


