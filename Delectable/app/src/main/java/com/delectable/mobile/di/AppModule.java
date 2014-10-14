package com.delectable.mobile.di;

import com.delectable.mobile.App;
import com.delectable.mobile.MainActivity;
import com.delectable.mobile.controllers.AccountController;
import com.delectable.mobile.controllers.BaseWineController;
import com.delectable.mobile.controllers.CaptureController;
import com.delectable.mobile.controllers.FoursquareController;
import com.delectable.mobile.controllers.MotdController;
import com.delectable.mobile.controllers.RegistrationController;
import com.delectable.mobile.controllers.VersionPropsFileController;
import com.delectable.mobile.controllers.WineScanController;
import com.delectable.mobile.data.AccountModel;
import com.delectable.mobile.data.BaseWineModel;
import com.delectable.mobile.data.Cache;
import com.delectable.mobile.data.CaptureDetailsListingModel;
import com.delectable.mobile.data.CaptureDetailsModel;
import com.delectable.mobile.data.CaptureListingModel;
import com.delectable.mobile.data.DeviceContactsModel;
import com.delectable.mobile.jobs.BaseJob;
import com.delectable.mobile.jobs.MyJobManager;
import com.delectable.mobile.jobs.accounts.AddIdentifierJob;
import com.delectable.mobile.jobs.accounts.AssociateFacebookJob;
import com.delectable.mobile.jobs.accounts.AssociateTwitterJob;
import com.delectable.mobile.jobs.accounts.FacebookifyProfilePhotoJob;
import com.delectable.mobile.jobs.accounts.FetchAccountCapturesJob;
import com.delectable.mobile.jobs.accounts.FetchAccountPrivateJob;
import com.delectable.mobile.jobs.accounts.FetchAccountProfileJob;
import com.delectable.mobile.jobs.accounts.FetchAccountsFromContactsJob;
import com.delectable.mobile.jobs.accounts.FetchActivityFeedJob;
import com.delectable.mobile.jobs.accounts.FetchDelectafriendsJob;
import com.delectable.mobile.jobs.accounts.FetchFacebookSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FetchFollowersJob;
import com.delectable.mobile.jobs.accounts.FetchFollowingsJob;
import com.delectable.mobile.jobs.accounts.FetchInfluencerSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FetchTwitterSuggestionsJob;
import com.delectable.mobile.jobs.accounts.FollowAccountJob;
import com.delectable.mobile.jobs.accounts.RemoveIdentifierJob;
import com.delectable.mobile.jobs.accounts.SearchAccountsJob;
import com.delectable.mobile.jobs.accounts.UpdateIdentifierJob;
import com.delectable.mobile.jobs.accounts.UpdateProfileJob;
import com.delectable.mobile.jobs.accounts.UpdateProfilePhotoJob;
import com.delectable.mobile.jobs.accounts.UpdateSettingJob;
import com.delectable.mobile.jobs.basewines.FetchBaseWineJob;
import com.delectable.mobile.jobs.basewines.SearchWinesJob;
import com.delectable.mobile.jobs.builddatecheck.FetchVersionPropsJob;
import com.delectable.mobile.jobs.captures.AddCaptureCommentJob;
import com.delectable.mobile.jobs.captures.DeleteCaptureJob;
import com.delectable.mobile.jobs.captures.EditCaptureCommentJob;
import com.delectable.mobile.jobs.captures.FetchCaptureDetailsJob;
import com.delectable.mobile.jobs.captures.FetchCaptureNotesJob;
import com.delectable.mobile.jobs.captures.FetchFollowerFeedJob;
import com.delectable.mobile.jobs.captures.FetchUserCaptureFeedJob;
import com.delectable.mobile.jobs.captures.LikeCaptureJob;
import com.delectable.mobile.jobs.captures.MarkCaptureHelpfulJob;
import com.delectable.mobile.jobs.captures.RateCaptureJob;
import com.delectable.mobile.jobs.foursquare.SearchFoursquareVenuesJob;
import com.delectable.mobile.jobs.motd.FetchMotdJob;
import com.delectable.mobile.jobs.registrations.LoginFacebookJob;
import com.delectable.mobile.jobs.registrations.LoginJob;
import com.delectable.mobile.jobs.registrations.RegisterJob;
import com.delectable.mobile.jobs.registrations.ResetPasswordJob;
import com.delectable.mobile.jobs.scanwinelabel.AddCaptureFromPendingCaptureJob;
import com.delectable.mobile.jobs.scanwinelabel.BasePhotoUploadJob;
import com.delectable.mobile.jobs.scanwinelabel.CreatePendingCaptureJob;
import com.delectable.mobile.jobs.scanwinelabel.IdentifyLabelJob;
import com.delectable.mobile.net.FoursquareNetworkClient;
import com.delectable.mobile.net.MotdNetworkClient;
import com.delectable.mobile.net.NetworkClient;
import com.delectable.mobile.net.S3ImageUploadNetworkClient;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.fragment.FoursquareVenueSelectionFragment;
import com.delectable.mobile.ui.camera.fragment.WineCaptureSubmitFragment;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.capture.fragment.CaptureDetailsFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowContactsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowExpertsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowFacebookFriendsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowTwitterFriendsTabFragment;
import com.delectable.mobile.ui.home.fragment.FollowFeedTabFragment;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.profile.fragment.FollowersFragment;
import com.delectable.mobile.ui.profile.fragment.FollowingFragment;
import com.delectable.mobile.ui.profile.fragment.RecentCapturesTabFragment;
import com.delectable.mobile.ui.profile.fragment.UserProfileFragment;
import com.delectable.mobile.ui.registration.dialog.ResetPasswordDialog;
import com.delectable.mobile.ui.registration.fragment.SignInFragment;
import com.delectable.mobile.ui.registration.fragment.SignUpFragment;
import com.delectable.mobile.ui.search.fragment.SearchPeopleTabFragment;
import com.delectable.mobile.ui.search.fragment.SearchWinesTabFragment;
import com.delectable.mobile.ui.settings.fragment.SettingsFragment;
import com.delectable.mobile.ui.tagpeople.fragment.TagPeopleFragment;
import com.delectable.mobile.ui.wineprofile.dialog.ChooseVintageDialog;
import com.delectable.mobile.ui.wineprofile.fragment.WineProfileFragment;
import com.iainconnor.objectcache.CacheManager;
import com.path.android.jobqueue.JobManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module(
        injects = {
                MainActivity.class,
                // Fragments
                BaseFragment.class,
                SignUpFragment.class,
                SignInFragment.class,
                NavigationDrawerFragment.class,
                UserProfileFragment.class,
                FollowersFragment.class,
                FollowingFragment.class,
                BaseCaptureDetailsFragment.class,
                CaptureDetailsFragment.class,
                RecentCapturesTabFragment.class,
                FollowFeedTabFragment.class,
                SettingsFragment.class,
                FollowExpertsTabFragment.class,
                FollowContactsTabFragment.class,
                FollowFacebookFriendsTabFragment.class,
                FollowTwitterFriendsTabFragment.class,
                TagPeopleFragment.class,
                FoursquareVenueSelectionFragment.class,
                WineCaptureSubmitFragment.class,
                SearchWinesTabFragment.class,
                SearchPeopleTabFragment.class,
                WineProfileFragment.class,
                // Dialogs
                ChooseVintageDialog.class,
                ResetPasswordDialog.class,
                // Models
                AccountModel.class,
                CaptureDetailsModel.class,
                CaptureDetailsListingModel.class,
                CaptureListingModel.class,
                DeviceContactsModel.class,
                BaseWineModel.class,
                // Jobs
                BaseJob.class,
                FetchMotdJob.class,
                FetchVersionPropsJob.class,
                FetchActivityFeedJob.class,
                FetchFollowerFeedJob.class,
                FetchUserCaptureFeedJob.class,
                LoginJob.class,
                RegisterJob.class,
                ResetPasswordJob.class,
                LoginFacebookJob.class,
                FetchCaptureDetailsJob.class,
                FetchAccountProfileJob.class,
                FetchAccountPrivateJob.class,
                FetchFollowersJob.class,
                FetchFollowingsJob.class,
                FetchAccountCapturesJob.class,
                FollowAccountJob.class,
                AddCaptureCommentJob.class,
                EditCaptureCommentJob.class,
                LikeCaptureJob.class,
                RateCaptureJob.class,
                DeleteCaptureJob.class,
                FetchInfluencerSuggestionsJob.class,
                FetchFacebookSuggestionsJob.class,
                FetchTwitterSuggestionsJob.class,
                SearchFoursquareVenuesJob.class,
                FetchDelectafriendsJob.class,
                FetchAccountsFromContactsJob.class,
                BasePhotoUploadJob.class,
                IdentifyLabelJob.class,
                CreatePendingCaptureJob.class,
                AddCaptureFromPendingCaptureJob.class,
                FacebookifyProfilePhotoJob.class,
                UpdateProfilePhotoJob.class,
                UpdateProfileJob.class,
                UpdateSettingJob.class,
                AddIdentifierJob.class,
                UpdateIdentifierJob.class,
                RemoveIdentifierJob.class,
                AssociateFacebookJob.class,
                AssociateTwitterJob.class,
                SearchWinesJob.class,
                SearchAccountsJob.class,
                FetchBaseWineJob.class,
                FetchCaptureNotesJob.class,
                MarkCaptureHelpfulJob.class,
                // Controllers
                MotdController.class,
                VersionPropsFileController.class,
                AccountController.class,
                CaptureController.class,
                RegistrationController.class,
                FoursquareController.class,
                WineScanController.class,
                BaseWineController.class
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

    @Provides
    @Singleton
    FoursquareNetworkClient provideFoursquareNetworkClient() {
        return new FoursquareNetworkClient();
    }

    @Provides
    @Singleton
    S3ImageUploadNetworkClient provideS3ImageUploadNetworkClient() {
        return new S3ImageUploadNetworkClient();
    }

    @Provides
    @Singleton
    MotdNetworkClient provideMotdNetworkClient() {
        return new MotdNetworkClient();
    }

}
