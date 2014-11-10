package com.delectable.mobile.util;

import com.delectable.mobile.App;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

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

    public static void shareBitmapInInstagram(Activity activity, Bitmap bitmap, String caption) {
        String url = MediaStore.Images.Media
                .insertImage(activity.getContentResolver(), bitmap, "DelectableCapture",
                        caption);
        Uri uri = Uri.parse(url);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, caption);
        // Must set instagram package, otherwise it'll ask what app to share with
        share.setPackage("com.instagram.android");
        //chooser needs to be created, or instagram won't launch into their sharing flow
        activity.startActivity(Intent.createChooser(share, ""));
    }
}
