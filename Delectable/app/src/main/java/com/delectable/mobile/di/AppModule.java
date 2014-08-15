package com.delectable.mobile.di;

import com.delectable.mobile.App;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.controllers.RegistrationController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.Cache;
import com.delectable.mobile.jobs.MyJobManager;
import com.delectable.mobile.jobs.accounts.FetchAccountJob;
import com.delectable.mobile.jobs.accounts.FollowAccountJob;
import com.delectable.mobile.jobs.registrations.LoginFacebookJob;
import com.delectable.mobile.jobs.registrations.LoginJob;
import com.delectable.mobile.jobs.registrations.RegisterJob;
import com.delectable.mobile.net.NetworkClient;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;
import com.delectable.mobile.ui.registration.fragment.LoginFragment;
import com.delectable.mobile.ui.registration.fragment.SignUpFragment;
import com.delectable.mobile.ui.settings.fragment.SettingsFragment;
import com.iainconnor.objectcache.CacheManager;
import com.path.android.jobqueue.JobManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module(
        injects = {
                BaseFragment.class,
                LoginFragment.class,
                SignUpFragment.class,
                NavigationDrawerFragment.class,
                UserProfileFragment.class,
                AccountModel.class,
                LoginJob.class,
                RegisterJob.class,
                LoginFacebookJob.class,
                FetchAccountJob.class,
                FollowAccountJob.class,
                AccountController.class,
                RegistrationController.class,
                SettingsFragment.class
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
    @Singleton
    NetworkClient provideNetworkClient() {
        return new NetworkClient();
    }

}
