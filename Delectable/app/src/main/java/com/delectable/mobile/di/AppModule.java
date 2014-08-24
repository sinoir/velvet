package com.delectable.mobile.di;

import com.delectable.mobile.App;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.controllers.RegistrationController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.Cache;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.MyJobManager;
import com.delectable.mobile.jobs.accounts.FetchAccountJob;
import com.delectable.mobile.jobs.accounts.FetchDelectafriendsJob;
import com.delectable.mobile.jobs.accounts.FetchInfluencerSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FollowAccountJob;
import com.delectable.mobile.jobs.accounts.oldFollowAccountJob;
import com.delectable.mobile.jobs.captures.AddCaptureCommentJob;
import com.delectable.mobile.jobs.captures.DeleteCaptureJob;
import com.delectable.mobile.jobs.captures.EditCaptureCommentJob;
import com.delectable.mobile.jobs.captures.FetchCaptureDetailsJob;
import com.delectable.mobile.jobs.captures.FetchFollowerFeedJob;
import com.delectable.mobile.jobs.captures.FetchUserCaptureFeedJob;
import com.delectable.mobile.jobs.captures.LikeCaptureJob;
import com.delectable.mobile.jobs.captures.RateCaptureJob;
import com.delectable.mobile.jobs.registrations.LoginFacebookJob;
import com.delectable.mobile.jobs.registrations.LoginJob;
import com.delectable.mobile.jobs.registrations.RegisterJob;
import com.delectable.mobile.net.NetworkClient;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.capture.fragment.CaptureDetailsFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowExpertsTabFragment;
import com.delectable.mobile.ui.home.fragment.FollowFeedTabFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.profile.fragment.RecentCapturesTabFragment;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;
import com.delectable.mobile.ui.registration.fragment.SignInFragment;
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
                // Fragments
                BaseFragment.class,
                SignUpFragment.class,
                SignInFragment.class,
                NavigationDrawerFragment.class,
                UserProfileFragment.class,
                BaseCaptureDetailsFragment.class,
                CaptureDetailsFragment.class,
                RecentCapturesTabFragment.class,
                FollowFeedTabFragment.class,
                SettingsFragment.class,
                FollowExpertsTabFragment.class,
                // Models
                AccountModel.class,
                CaptureDetailsModel.class,
                CaptureDetailsListingModel.class,
                // Jobs
                BaseJob.class,
                FetchFollowerFeedJob.class,
                FetchUserCaptureFeedJob.class,
                LoginJob.class,
                RegisterJob.class,
                LoginFacebookJob.class,
                FetchCaptureDetailsJob.class,
                FetchAccountJob.class,
                oldFollowAccountJob.class,
                FollowAccountJob.class,
                AddCaptureCommentJob.class,
                EditCaptureCommentJob.class,
                LikeCaptureJob.class,
                RateCaptureJob.class,
                DeleteCaptureJob.class,
                FetchInfluencerSuggestionsJob.class,
                FetchDelectafriendsJob.class,
                // Controllers
                AccountController.class,
                CaptureController.class,
                RegistrationController.class
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
