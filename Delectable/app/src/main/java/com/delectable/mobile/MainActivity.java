package com.delectable.mobile;

import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
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
            launchIntent.setClass(getApplicationContext(), NavActivity.class);
        } else {
            launchIntent.setClass(getApplicationContext(), LoginActivity.class);
        }
        startActivity(launchIntent);

        finish();
    }
}
