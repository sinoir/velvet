package com.delectable.mobile.util;

import com.delectable.mobile.App;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstagramUtil {

    private static final String TAG = InstagramUtil.class.getSimpleName();

    private static Pattern sMentionPattern = Pattern.compile("@\\w+");

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
        if (bitmap == null) {
            return;
        }
        String url = MediaStore.Images.Media
                .insertImage(activity.getContentResolver(), bitmap, "DelectableCapture",
                        caption);
        Uri uri = Uri.parse(url);

        // remove @mentions from caption to avoid conflicts
        Log.d(TAG, "raw caption: " + caption);
        Matcher matcher = sMentionPattern.matcher(caption);
        while (matcher.find()) {
            int spanStart = matcher.start();
            int spanEnd = matcher.end();
            String mention = caption
                    .substring(spanStart, spanEnd);
            caption = caption.replace(mention, mention.substring(1)); // remove @ symbol
            matcher = sMentionPattern.matcher(caption); // start over because we changed the text
        }
        Log.d(TAG, "cleaned caption: " + caption);

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
