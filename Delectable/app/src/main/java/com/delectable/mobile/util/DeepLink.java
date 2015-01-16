package com.delectable.mobile.util;

import com.delectable.mobile.api.models.CaptureFeed;
import com.delectable.mobile.ui.camera.activity.WineCaptureActivity;
import com.delectable.mobile.ui.capture.activity.CaptureDetailsActivity;
import com.delectable.mobile.ui.capture.activity.FeedActivity;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.profile.activity.UserProfileActivity;
import com.delectable.mobile.ui.wineprofile.activity.WineProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.util.HashMap;

public enum DeepLink {

    ACCOUNT("account"),
    CAPTURES("captures"),
    BASE_WINE("base_wine"),
    CAPTURE("capture"),
    FEED("feed"),
    CUSTOM_FEED("custom_feed"),
    CAMERA("camera"),
    OPEN("open"),
    UNKNOWN("unknown");

    //TODO implement
    //REGION("region"),
    //PURCHASE("purchase"),
    //FEED("feed"),

    private static final String TAG = DeepLink.class.getSimpleName();

    private String mHostName;

    private DeepLink(String hostName) {
        mHostName = hostName;
    }

    public String getHostName() {
        return mHostName;
    }

    private final static HashMap<String, DeepLink> BY_HOSTNAME_MAP
            = new HashMap<String, DeepLink>();

    static {
        for (DeepLink deepLink : DeepLink.values()) {
            BY_HOSTNAME_MAP.put(deepLink.getHostName(), deepLink);
        }
    }

    public static DeepLink valueOfHost(String hostName) {
        if (hostName == null || hostName.isEmpty()) {
            return UNKNOWN;
        }
        DeepLink deepLink = BY_HOSTNAME_MAP.get(hostName);
        if (deepLink == null) {
            return UNKNOWN;
        }
        return deepLink;
    }

    public static DeepLink valueOf(Uri data) {
        if (data == null) {
            return UNKNOWN;
        }

        //TODO remove when this case is handled
        //currently unable to handle base_wine deeplink without a base_wine_id (just a vintage_id param)
        if (DeepLink.BASE_WINE == valueOfHost(data.getHost())) {
            String baseWineId = data.getQueryParameter("base_wine_id");
            if (baseWineId == null) {
                return UNKNOWN;
            }
        }

        return valueOfHost(data.getHost());
    }

    /**
     * @return Returns {@code null} if Uri does not contain valid data to produce an intent.
     */
    public static Intent getIntent(Context c, Uri data) {
        DeepLink deeplink = valueOf(data);
        switch (deeplink) {
            case ACCOUNT:
                return prepareUserProfile(c, data);
            case CAPTURES:
                return prepareUserProfile(c, data);
            case BASE_WINE:
                return prepareWineProfile(c, data);
            case CAPTURE:
                return prepareCaptureDetails(c, data);
            case FEED:
                //the feeds are encapsulated in the feedactivity, there is no special intent
                return prepareFeed(c, data);
            case CUSTOM_FEED:
                return prepareCustomFeed(c, data);
            case CAMERA:
                return prepareCamera(c);
            case OPEN:
                //no need to prepare intent, open simply opens the app
            case UNKNOWN:
            default:
                return null;
        }
    }

    private static Intent prepareUserProfile(Context c, Uri data) {
        //data looks like this: delectable://account?account_id=532218501d2b112e5c000163
        String accountId = data.getQueryParameter("account_id");
        return UserProfileActivity.newIntent(c, accountId);
    }

    private static Intent prepareWineProfile(Context c, Uri data) {
        String baseWineId = data.getQueryParameter("base_wine_id");
        String vintageId = data.getQueryParameter("vintage_id");
        return WineProfileActivity.newIntent(c, baseWineId, vintageId);
    }

    private static Intent prepareCaptureDetails(Context c, Uri data) {
        String captureId = data.getQueryParameter("capture_id");
        return CaptureDetailsActivity.newIntent(c, captureId);
    }

    private static Intent prepareFeed(Context c, Uri data) {
        String feedKey = data.getQueryParameter("feed_key");
        return NavActivity.newFeedIntent(c, feedKey);
    }

    private static Intent prepareCustomFeed(Context c, Uri data) {
        String feedKey = data.getQueryParameter("feed_key");
        return FeedActivity.newIntent(c, feedKey, CaptureFeed.CUSTOM, "");
    }
    private static Intent prepareCamera(Context c) {
        return new Intent(c, WineCaptureActivity.class);

    }
}
