package com.delectable.mobile;

import com.delectable.mobile.data.UserInfo;
import com.delectable.mobile.di.AppModule;
import com.kahuna.sdk.KahunaAnalytics;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import android.app.Application;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import dagger.ObjectGraph;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App sInstance;

    private ObjectGraph mObjectGraph;

    private static final int API_KEY = 0;

    private static final int API_SECRET = 1;

    public App() {
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }

    public static void injectMembers(Object object) {
        getInstance().mObjectGraph.inject(object);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            KahunaAnalytics
                    .onAppCreate(this, BuildConfig.KAHUNA_SECRET, BuildConfig.KAHUNA_PUSH_ID);
            KahunaAnalytics.setPushReceiver(PushReceiver.class);
            updateKahunaAttributes();
        } catch (Exception ex) {
            Log.wtf(TAG, "Kahuna Failed", ex);
        }

        mObjectGraph = ObjectGraph.create(new AppModule());

        String[] twitter = getTwitterApiKeyAndSecret();
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(
                        twitter[API_KEY],
                        twitter[API_SECRET]);
        Fabric.with(this,
                new Twitter(authConfig));
    }

    public void updateKahunaAttributes() {
        if (UserInfo.isSignedIn(this)) {
            String username = UserInfo.getUserId(this);
            String email = UserInfo.getUserEmail(this);

            KahunaAnalytics.setUsernameAndEmail(username, email);
            // TODO: Enable / Disable dialog in Settings?
            KahunaAnalytics.enablePush();
        }
    }

    private String[] getTwitterApiKeyAndSecret() {
        String apiKey = null;
        String apiSecret = null;
        try {
            AssetManager assetManager = getAssets();
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
}
