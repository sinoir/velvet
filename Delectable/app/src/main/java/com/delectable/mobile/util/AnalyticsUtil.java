package com.delectable.mobile.util;

import com.delectable.mobile.App;
import com.delectable.mobile.BuildConfig;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.util.Log;

public class AnalyticsUtil {

    // Account Type
    public static final String ACCOUNT_TYPE = "Account type";

    public static final String ACCOUNT_FACEBOOK = "facebook";

    public static final String ACCOUNT_EMAIL = "email";

    // Feed
    public static final String FEED = "Feed";

    public static final String FEED_FOLLOWING = "Following";

    public static final String FEED_TRENDING = "Trending";

    public static final String FEED_THANKSGIVING = "Thanksgiving";

    // User Profile
    public static final String USER_PROFILE_TYPE = "Type";

    public static final String USER_PROFILE_OWN = "Own";

    public static final String USER_PROFILE_OTHERS = "Others";

    // Activity
    public static final String ACTIVITY_TYPE = "Type";

    public static final String ACTIVITY_COMMENT = "Comment";

    public static final String ACTIVITY_FOLLOW = "Follow";

    public static final String ACTIVITY_NEWACCOUNT = "NewAccount";

    public static final String ACTIVITY_TAGGING = "Tagging";

    public static final String ACTIVITY_LIKE = "Like";

    public static final String ACTIVITY_TRANSCRIPTION = "Transcription";

    public static final String ACTIVITY_HELPFUL = "Helpful";

    // Search
    public static final String SEARCH_TYPE = "Search Type";

    public static final String SEARCH_WINE = "wine";

    public static final String SEARCH_PEOPLE = "people";

    // Photo
    public static final String PHOTO_TYPE = "Photo type";

    public static final String PHOTO_NEW = "new photo";

    public static final String PHOTO_CAMERA_ROLL = "camera roll";

    private static final String TAG = AnalyticsUtil.class.getSimpleName();

    private MixpanelAPI mixpanel = MixpanelAPI
            .getInstance(App.getInstance(), BuildConfig.MIXPANEL_TOKEN);

    public AnalyticsUtil() {
        setSuperProperties(null);
    }

    public void flush() {
        mixpanel.flush();
    }

    public void identify(String userId) {
        setSuperProperties(userId);
        mixpanel.identify(userId);
        Log.d(TAG, "identify: " + userId);
    }

    /**
     * Call this only ONCE per user, after they signed up
     */
    public void alias(String userId) {
        mixpanel.alias(userId, null);
        mixpanel.identify(userId);
        Log.d(TAG, "alias: " + userId);
    }

    public void setSuperProperties(String accountId) {
        JSONObject props = new JSONObject();
        try {
            props.put("zSP-OS", "Android");
            props.put("zSP-Version-OS", Build.VERSION.RELEASE);
            props.put("zSP-Version-Device", Build.MANUFACTURER + " " + Build.MODEL);
            props.put("zSP-Version-App",
                    BuildConfig.VERSION_NAME + " rv:" + BuildConfig.VERSION_CODE);
            props.put("zSP-AccountID", accountId);
            props.put("zSP-UDID", mixpanel.getDistinctId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.registerSuperProperties(props);
        Log.d(TAG, "setSuperProperties");
    }

    public void trackRegister(String accountType) {
        JSONObject props = new JSONObject();
        try {
            props.put(ACCOUNT_TYPE, accountType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Install-Mobile-04-Register", props);
        Log.d(TAG, "trackRegister: " + accountType);
    }

    public void trackSwitchFeed(String feed) {
        JSONObject props = new JSONObject();
        try {
            props.put(FEED, feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Nav-Mobile-Switch feeds", props);
        Log.d(TAG, "trackSwitchFeed: " + feed);
    }

    public void trackViewItemInFeed(String feed) {
        JSONObject props = new JSONObject();
        try {
            props.put(FEED, feed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Feed-Mobile-Item visible", props);
    }

    public void trackViewWineProfile() {
        mixpanel.track("Wine profile-Mobile-View wine profile", null);
    }

    public void trackViewCaptureDetails() {
        mixpanel.track("Capture-Mobile-View a capture", null);
    }

    public void trackViewUserProfile(String type) {
        JSONObject props = new JSONObject();
        try {
            props.put(USER_PROFILE_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("View user profile", props);
    }

    public void trackActivity(String type) {
        JSONObject props = new JSONObject();
        try {
            props.put(ACTIVITY_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Activity-Mobile-Click Activity item", props);
    }

    public void trackSearch(String type) {
        JSONObject props = new JSONObject();
        try {
            props.put(SEARCH_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Explore-Mobile-Search query", props);
    }

    public void trackScan(String photoType) {
        JSONObject props = new JSONObject();
        try {
            props.put(PHOTO_TYPE, photoType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.track("Scan-Mobile-02-Scan submitted", props);
    }

    public void trackRate() {
        mixpanel.track("Rated-Mobile-Rated wine", null);
    }

    public void trackFollowUser() {
        mixpanel.track("Follow-Mobile-Following", null);
    }

}
