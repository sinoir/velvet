package com.delectable.mobile.util;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class TwitterUtil {

    private static final String TAG = TwitterUtil.class.getSimpleName();

    private static final int API_KEY = 0;

    private static final int API_SECRET = 1;

    public static TwitterAuthConfig getAuthConfig(Context context) {
        String[] twitter = getTwitterApiKeyAndSecret(context);
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(
                        twitter[API_KEY],
                        twitter[API_SECRET]);
        //Log.d(TAG, "twitter apikey: " + twitter[API_KEY]);
        //Log.d(TAG, "twitter apisecret: " + twitter[API_SCRET]);
        return authConfig;
    }

    private static String[] getTwitterApiKeyAndSecret(Context context) {
        String apiKey = null;
        String apiSecret = null;
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("twitter_credentials.properties");

            Properties properties = new Properties();
            properties.load(inputStream);
            apiKey = properties.getProperty("API_KEY");
            apiSecret = properties.getProperty("API_SECRET");

        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] twitterCreds = new String[2];
        twitterCreds[API_KEY] = apiKey;
        twitterCreds[API_SECRET] = apiSecret;
        return twitterCreds;
    }

    /**
     * Checks to see that there is an active twitter session.
     */
    public static boolean isLoggedIn() {
        if (Twitter.getSessionManager().getActiveSession() == null) {
            return false;
        }
        return true;
    }

    public static void clearSession() {
        Twitter.getSessionManager().clearActiveSession();
    }

    public static void tweet(String message, Callback<Tweet> callback) {
        TwitterApiClient twitterApiClient = Twitter.getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        statusesService.update(message, null, null, null, null, null, null, null, callback);
    }

    public static void debug() {
        long userId = Twitter.getSessionManager().getActiveSession().getUserId();
        String userName = Twitter.getSessionManager().getActiveSession().getUserName();
        String authToken = Twitter.getSessionManager().getActiveSession().getAuthToken().toString();

        Log.d(TAG, "userid: " + userId);
        Log.d(TAG, "userName: " + userName);
        Log.d(TAG, "authToken: " + authToken);

        tweet("Hello from the delectable android app! " + new Date().toString(), TwitterCallback);
    }

    private static Callback<Tweet> TwitterCallback = new Callback<Tweet>() {
        @Override
        public void success(Result<Tweet> tweetResult) {
            Log.d(TAG, "tweet success!");
        }

        @Override
        public void failure(TwitterException e) {
            Log.d(TAG, "tweet fail");
            Log.d(TAG, "TwitterException", e);
        }
    };


}
