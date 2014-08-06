package com.delectable.mobile.di;

import com.delectable.mobile.App;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.Cache;
import com.delectable.mobile.jobs.FetchAccountJob;
import com.delectable.mobile.jobs.MyJobManager;
import com.delectable.mobile.net.NetworkClient;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.google.gson.Gson;
import com.iainconnor.objectcache.CacheManager;
import com.path.android.jobqueue.JobManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module(
        injects = {
                BaseFragment.class,
                NavigationDrawerFragment.class,
                AccountModel.class,
                FetchAccountJob.class,
                AccountController.class
        }
)
public class AppModule {

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    JobManager provideJobManager() {
        return new MyJobManager(App.getInstance());
    }

    @Provides
    @Singleton
    CacheManager provideCacheManager() {
        Cache.init(App.getInstance());
        return Cache.getCacheManager();
    }

    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    NetworkClient provideNetworkClient() {
        return new NetworkClient();
    }

}
