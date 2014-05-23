package com.delectable.mobile;

import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.home.activity.HomeActivity;
import com.delectable.mobile.ui.registration.activity.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent launchIntent = new Intent();
        if (UserInfo.isSignedIn(this)) {
            launchIntent.setClass(getApplicationContext(), HomeActivity.class);
        } else {
            launchIntent.setClass(getApplicationContext(), LoginActivity.class);
        }
        startActivity(launchIntent);

        finish();
    }
}
