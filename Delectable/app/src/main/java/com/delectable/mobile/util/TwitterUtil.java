package com.delectable.mobile.util;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import android.util.Log;

import java.util.Date;

public class TwitterUtil {

    private static final String TAG = TwitterUtil.class.getSimpleName();

    /**
     * Checks to see that there is an active twitter session.
     * @return
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

    public static void debug() {
        long userId = Twitter.getSessionManager().getActiveSession().getUserId();
        String userName = Twitter.getSessionManager().getActiveSession().getUserName();
        String authToken = Twitter.getSessionManager().getActiveSession().getAuthToken().toString();

        Log.d(TAG, "userid: " + userId);
        Log.d(TAG, "userName: " + userName);
        Log.d(TAG, "authToken: " + authToken);

        //shoots out tweet
        TwitterApiClient twitterApiClient = Twitter.getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        String tweet = "Hello from the delectable android app! " + new Date().toString();
        statusesService
                .update(tweet, null, null, null, null, null, null, null, new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> tweetResult) {
                        Log.d(TAG, "tweet success!");
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.d(TAG, "tweet fail");
                        Log.d(TAG, "TwitterException", e);
                        e.getMessage();
                    }
                });
    }


}
