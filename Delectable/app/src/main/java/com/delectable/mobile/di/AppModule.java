package com.delectable.mobile.di;

import com.delectable.mobile.App;
import com.delectable.mobile.MainActivity;
import com.delectable.mobile.api.cache.AccountModel;
import com.delectable.mobile.api.cache.BaseWineModel;
import com.delectable.mobile.api.cache.CaptureDetailsModel;
import com.delectable.mobile.api.cache.CaptureListingModel;
import com.delectable.mobile.api.cache.CapturesPendingCapturesListingModel;
import com.delectable.mobile.api.cache.DeviceContactsModel;
import com.delectable.mobile.api.cache.FollowersFollowingModel;
import com.delectable.mobile.api.cache.PendingCapturesModel;
import com.delectable.mobile.api.cache.WineSourceModel;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.controllers.BaseWineController;
import com.delectable.mobile.api.controllers.CaptureController;
import com.delectable.mobile.api.controllers.FoursquareController;
import com.delectable.mobile.api.controllers.MotdController;
import com.delectable.mobile.api.controllers.RegistrationController;
import com.delectable.mobile.api.controllers.VersionPropsFileController;
import com.delectable.mobile.api.controllers.WineScanController;
import com.delectable.mobile.api.jobs.BaseJob;
import com.delectable.mobile.api.jobs.MyJobManager;
import com.delectable.mobile.api.jobs.accounts.AddIdentifierJob;
import com.delectable.mobile.api.jobs.accounts.AddPaymentMethodJob;
import com.delectable.mobile.api.jobs.accounts.AddShippingAddressJob;
import com.delectable.mobile.api.jobs.accounts.AssociateFacebookJob;
import com.delectable.mobile.api.jobs.accounts.AssociateTwitterJob;
import com.delectable.mobile.api.jobs.accounts.FacebookifyProfilePhotoJob;
import com.delectable.mobile.api.jobs.accounts.FetchAccountCapturesJob;
import com.delectable.mobile.api.jobs.accounts.FetchAccountPrivateJob;
import com.delectable.mobile.api.jobs.accounts.FetchAccountProfileJob;
import com.delectable.mobile.api.jobs.accounts.FetchAccountsFromContactsJob;
import com.delectable.mobile.api.jobs.accounts.FetchActivityFeedJob;
import com.delectable.mobile.api.jobs.accounts.FetchDelectafriendsJob;
import com.delectable.mobile.api.jobs.accounts.FetchFacebookSuggestionsJob;
import com.delectable.mobile.api.jobs.accounts.FetchFollowerFeedJob;
import com.delectable.mobile.api.jobs.accounts.FetchFollowersJob;
import com.delectable.mobile.api.jobs.accounts.FetchFollowingsJob;
import com.delectable.mobile.api.jobs.accounts.FetchInfluencerSuggestionsJob;
import com.delectable.mobile.api.jobs.accounts.FetchPaymentMethodJob;
import com.delectable.mobile.api.jobs.accounts.FetchShippingAddressesJob;
import com.delectable.mobile.api.jobs.accounts.FetchTwitterSuggestionsJob;
import com.delectable.mobile.api.jobs.accounts.FollowAccountJob;
import com.delectable.mobile.api.jobs.accounts.RemoveIdentifierJob;
import com.delectable.mobile.api.jobs.accounts.RemovePaymentMethodJob;
import com.delectable.mobile.api.jobs.accounts.RemoveShippingAddressJob;
import com.delectable.mobile.api.jobs.accounts.SearchAccountsJob;
import com.delectable.mobile.api.jobs.accounts.SetPrimaryPaymentMethodJob;
import com.delectable.mobile.api.jobs.accounts.SetPrimaryShippingAddressJob;
import com.delectable.mobile.api.jobs.accounts.UpdateIdentifierJob;
import com.delectable.mobile.api.jobs.accounts.UpdateProfileJob;
import com.delectable.mobile.api.jobs.accounts.UpdateProfilePhotoJob;
import com.delectable.mobile.api.jobs.accounts.UpdateSettingJob;
import com.delectable.mobile.api.jobs.accounts.UpdateShippingAddressJob;
import com.delectable.mobile.api.jobs.basewines.FetchBaseWineJob;
import com.delectable.mobile.api.jobs.basewines.FetchWineSourceJob;
import com.delectable.mobile.api.jobs.basewines.PurchaseWineJob;
import com.delectable.mobile.api.jobs.basewines.SearchWinesJob;
import com.delectable.mobile.api.jobs.builddatecheck.FetchVersionPropsJob;
import com.delectable.mobile.api.jobs.captures.AddCaptureCommentJob;
import com.delectable.mobile.api.jobs.captures.DeleteCaptureJob;
import com.delectable.mobile.api.jobs.captures.EditCaptureCommentJob;
import com.delectable.mobile.api.jobs.captures.FetchCaptureDetailsJob;
import com.delectable.mobile.api.jobs.captures.FetchCaptureNotesJob;
import com.delectable.mobile.api.jobs.captures.FetchTrendingCapturesJob;
import com.delectable.mobile.api.jobs.captures.FlagCaptureJob;
import com.delectable.mobile.api.jobs.captures.LikeCaptureJob;
import com.delectable.mobile.api.jobs.captures.MarkCaptureHelpfulJob;
import com.delectable.mobile.api.jobs.captures.RateCaptureJob;
import com.delectable.mobile.api.jobs.foursquare.SearchFoursquareVenuesJob;
import com.delectable.mobile.api.jobs.motd.FetchMotdJob;
import com.delectable.mobile.api.jobs.registrations.LoginFacebookJob;
import com.delectable.mobile.api.jobs.registrations.LoginJob;
import com.delectable.mobile.api.jobs.registrations.RegisterJob;
import com.delectable.mobile.api.jobs.registrations.ResetPasswordJob;
import com.delectable.mobile.api.jobs.scanwinelabel.AddCaptureFromPendingCaptureJob;
import com.delectable.mobile.api.jobs.scanwinelabel.BasePhotoUploadJob;
import com.delectable.mobile.api.jobs.scanwinelabel.CreatePendingCaptureJob;
import com.delectable.mobile.api.jobs.scanwinelabel.IdentifyLabelJob;
import com.delectable.mobile.api.net.FoursquareNetworkClient;
import com.delectable.mobile.api.net.MotdNetworkClient;
import com.delectable.mobile.api.net.NetworkClient;
import com.delectable.mobile.api.net.S3ImageUploadNetworkClient;
import com.delectable.mobile.ui.BaseFragment;
import com.delectable.mobile.ui.camera.fragment.FoursquareVenueSelectionFragment;
import com.delectable.mobile.ui.camera.fragment.WineCaptureSubmitFragment;
import com.delectable.mobile.ui.capture.fragment.BaseCaptureDetailsFragment;
import com.delectable.mobile.ui.capture.fragment.CaptureDetailsFragment;
import com.delectable.mobile.ui.capture.fragment.TaggedPeopleFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowContactsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowExpertsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowFacebookFriendsTabFragment;
import com.delectable.mobile.ui.followfriends.fragment.FollowTwitterFriendsTabFragment;
import com.delectable.mobile.ui.home.fragment.FollowerFeedTabFragment;
import com.delectable.mobile.ui.home.fragment.TrendingTabFragment;
import com.delectable.mobile.ui.navigation.activity.NavActivity;
import com.delectable.mobile.ui.navigation.fragment.NavigationDrawerFragment;
import com.delectable.mobile.ui.profile.fragment.FollowersFragment;
import com.delectable.mobile.ui.profile.fragment.FollowingFragment;
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
import com.delectable.mobile.ui.winepurchase.dialog.AddPaymentMethodDialog;
import com.delectable.mobile.ui.winepurchase.dialog.AddShippingAddressDialog;
import com.delectable.mobile.ui.winepurchase.dialog.ChoosePaymentMethodDialog;
import com.delectable.mobile.ui.winepurchase.dialog.ChooseShippingAddressDialog;
import com.delectable.mobile.ui.winepurchase.fragment.ConfirmationFragment;
import com.delectable.mobile.ui.winepurchase.fragment.WineCheckoutFragment;
import com.path.android.jobqueue.JobManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module(
        injects = {
                MainActivity.class,
                NavActivity.class,
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
                FollowerFeedTabFragment.class,
                TrendingTabFragment.class,
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
                TaggedPeopleFragment.class,
                WineCheckoutFragment.class,
                ConfirmationFragment.class,
                // Dialogs
                ChooseVintageDialog.class,
                ResetPasswordDialog.class,
                AddShippingAddressDialog.class,
                AddPaymentMethodDialog.class,
                ChooseShippingAddressDialog.class,
                ChoosePaymentMethodDialog.class,
                // Models
                AccountModel.class,
                CaptureDetailsModel.class,
                CaptureListingModel.class,
                FollowersFollowingModel.class,
                DeviceContactsModel.class,
                BaseWineModel.class,
                WineSourceModel.class,
                // Jobs
                BaseJob.class,
                FetchMotdJob.class,
                FetchVersionPropsJob.class,
                FetchActivityFeedJob.class,
                FetchFollowerFeedJob.class,
                FetchTrendingCapturesJob.class,
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
                FlagCaptureJob.class,
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
                FetchWineSourceJob.class,
                FetchShippingAddressesJob.class,
                AddShippingAddressJob.class,
                UpdateShippingAddressJob.class,
                RemoveShippingAddressJob.class,
                SetPrimaryShippingAddressJob.class,
                FetchPaymentMethodJob.class,
                AddPaymentMethodJob.class,
                SetPrimaryPaymentMethodJob.class,
                RemovePaymentMethodJob.class,
                PurchaseWineJob.class,
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
    AccountModel provideAccountModel() {
        return new AccountModel();
    }

    @Provides
    @Singleton
    CapturesPendingCapturesListingModel provideCapturesPendingCapturesListingModel(
            PendingCapturesModel pendingCapturesModel, CaptureDetailsModel capturesModel) {
        return new CapturesPendingCapturesListingModel(pendingCapturesModel, capturesModel);
    }

    @Provides
    @Singleton
    PendingCapturesModel providePendingCapturesModel() {
        return new PendingCapturesModel();
    }


    @Provides
    @Singleton
    CaptureDetailsModel provideCaptureDetailsModel() {
        return new CaptureDetailsModel();
    }

    @Provides
    @Singleton
    BaseWineModel provideBaseWineModel() {
        return new BaseWineModel();
    }

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
