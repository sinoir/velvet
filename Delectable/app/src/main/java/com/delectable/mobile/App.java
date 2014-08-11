package com.delectable.mobile;

import android.app.Application;

import com.delectable.mobile.di.AppModule;

import dagger.ObjectGraph;

public class App extends Application {

    private static App sInstance;
    private ObjectGraph mObjectGraph;

    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(new AppModule());
    }

    public static App getInstance() {
        return sInstance;
    }

    public static void injectMembers(Object object) {
        getInstance().mObjectGraph.inject(object);
    }

}
