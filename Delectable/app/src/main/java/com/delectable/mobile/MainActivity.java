package com.delectable.mobile;

import com.delectable.mobile.controllers.VersionPropsFileController;
import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.events.builddatecheck.BuildDateCheckedEvent;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.registration.activity.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;


public class MainActivity extends Activity {

    @Inject
    VersionPropsFileController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);

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
        if (event.isSuccessful()) {
            if (event.shouldUpdate()) {
                //TODO go to activity to let user update
            } else {
                launchNavOrLogin();
            }
            return;
        }
        //show error
        Toast.makeText(this, event.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


}


