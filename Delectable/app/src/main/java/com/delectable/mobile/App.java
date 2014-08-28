package com.delectable.mobile;

import com.delectable.mobile.di.AppModule;

import android.app.Application;

import dagger.ObjectGraph;

public class App extends Application {

    private static App sInstance;

    private ObjectGraph mObjectGraph;

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

        // TODO : Add in Appropriate API Keys and put them in the BuildConfig Gradle 1 for release and 1 for debug keystores
//        KahunaAnalytics.onAppCreate(this, "", "YOUR_SENDER_ID");
//        KahunaAnalytics.setPushReceiver(PushReceiver.class);

        mObjectGraph = ObjectGraph.create(new AppModule());
    }

}
