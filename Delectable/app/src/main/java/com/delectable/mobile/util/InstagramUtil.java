package com.delectable.mobile.util;

import com.delectable.mobile.App;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class InstagramUtil {

    public static boolean isInstagramAvailable() {
        boolean appInstalled = false;
        try {
            ApplicationInfo info = App.getInstance().getPackageManager()
                    .getApplicationInfo("com.instagram.android", 0);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appInstalled = false;
        }
        return appInstalled;
    }
}
