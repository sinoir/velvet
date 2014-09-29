package com.delectable.mobile.util;

import com.kahuna.sdk.KahunaAnalytics;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KahunaUtil {

    public static void trackStart() {
        trackKahunaEvent("start", new HashMap<String, String>());
    }

    public static void trackSignUp(String signupMethod, String firstName, String lastName,
            Date date) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("sign_up_method", signupMethod);
        attributes.put("first_name", firstName);
        attributes.put("last_name", lastName);
        attributes.put("creation_date", formatDateForKahuna(date));
        trackKahunaEvent("sign_up", attributes);
    }

    public static void trackLogin(String accountId, String email) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("username", accountId);
        attributes.put("email", email);
        trackKahunaEvent("login", attributes);
    }

    public static void trackLogOut() {
        HashMap<String, String> attributes = new HashMap<String, String>();
        trackKahunaEvent("logout", attributes);
        KahunaAnalytics.logout();
    }

    public static void trackFollowUser(String numFollowing, String lastUserName,
            String lastUserId) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("num_following", numFollowing);
        attributes.put("last_user_followed_name", lastUserName);
        attributes.put("last_user_followed_id", lastUserId);

        trackKahunaEvent("follow_user", attributes);
    }

    public static void trackSearch() {
        HashMap<String, String> attributes = new HashMap<String, String>();
        trackKahunaEvent("search", attributes);
    }

    public static void trackScanBottle(Date scanDate) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("date_of_last_scan", formatDateForKahuna(scanDate));
        trackKahunaEvent("scan_bottle", attributes);
    }

    public static void trackCreateCapture(Date captureDate, String createdCaptureId) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("date_of_last_capture", formatDateForKahuna(captureDate));
        attributes.put("last_capture_created_id", createdCaptureId);
        trackKahunaEvent("create_capture", attributes);
    }

    public static void trackViewWine(String wineId, String wineName) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("last_wine_viewed_id", wineId);
        attributes.put("last_wine_viewed_name", wineName);
        trackKahunaEvent("view_wine", attributes);
    }

    public static void trackAddWineToWishlist(String wineId, String wineName) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("last_wine_added_id", wineId);
        attributes.put("last_wine_added_name", wineName);
        trackKahunaEvent("add_wine_to_wishlist", attributes);
    }

    public static void trackShareWine(String wineId, String wineName) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("last_wine_shared_id", wineId);
        attributes.put("last_wine_shared_name", wineName);
        trackKahunaEvent("share_wine", attributes);
    }

    public static void trackPurchaseWine(String wineId, String wineName) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("last_wine_purchased_id", wineId);
        attributes.put("last_wine_purchased_name", wineName);
        trackKahunaEvent("purchase_wine", attributes);
    }

    public static void trackComment() {
        HashMap<String, String> attributes = new HashMap<String, String>();
        trackKahunaEvent("comment", attributes);
    }

    public static void trackLikeCapture(String captureId, String capturedWineName) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("last_capture_liked_id", captureId);
        attributes.put("last_capture_liked_name", capturedWineName);
        trackKahunaEvent("like_capture", attributes);
    }

    public static void trackHelpfulComment() {
        // TODO: Add this to HelpfulJob once job is created
        HashMap<String, String> attributes = new HashMap<String, String>();
        trackKahunaEvent("helpful_a_comment", attributes);
    }

    public static void trackKahunaEvent(String event, Map<String, String> attributes) {
        String tag = "KahunaUtil.trackKahunaEvent";
        Log.i(tag, "Event: " + event);
        Log.i(tag, "Attributes: " + attributes);
        KahunaAnalytics.trackEvent(event);
        KahunaAnalytics.setUserAttributes(attributes);
    }

    private static String formatDateForKahuna(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

}
