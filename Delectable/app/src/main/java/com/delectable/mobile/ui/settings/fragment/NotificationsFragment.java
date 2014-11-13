package com.delectable.mobile.ui.settings.fragment;

import com.delectable.mobile.App;
import com.delectable.mobile.R;
import com.delectable.mobile.api.cache.UserInfo;
import com.delectable.mobile.api.controllers.AccountController;
import com.delectable.mobile.api.events.accounts.UpdatedSettingEvent;
import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.api.models.AccountConfig.Key;
import com.delectable.mobile.ui.BaseFragment;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationsFragment extends BaseFragment {

    public static final String TAG = NotificationsFragment.class.getSimpleName();

    private Account mAccount;

    private View mView;

    /**
     * Maps the view to the accountconfig property.
     */
    private static final HashMap<Integer, Key> VIEW_KEY_MAP = new HashMap<Integer, Key>();

    static {
        VIEW_KEY_MAP.put(R.id.wine_identification_switch, Key.PN_CAPTURE_TRANSCRIBED);
        VIEW_KEY_MAP.put(R.id.tagged_on_a_wine_switch, Key.PN_TAGGED);
        VIEW_KEY_MAP.put(R.id.comment_on_your_wine_switch, Key.PN_COMMENT_ON_OWN_WINE);
        VIEW_KEY_MAP.put(R.id.respond_to_comment_switch, Key.PN_COMMENT_RESPONSE);
        VIEW_KEY_MAP.put(R.id.like_your_wine_switch, Key.PN_LIKE_ON_OWN_WINE);
        VIEW_KEY_MAP.put(R.id.following_you_switch, Key.PN_NEW_FOLLOWER);
        VIEW_KEY_MAP.put(R.id.friend_joined_delectable_switch, Key.PN_FRIEND_JOINED);
    }

    @Inject
    protected AccountController mAccountController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.injectMembers(this);
        mAccount = UserInfo.getAccountPrivate(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.inject(this, mView);
        refreshViews();
        return mView;
    }

    private void refreshViews() {
        for (Map.Entry<Integer, Key> entry : VIEW_KEY_MAP.entrySet()) {
            //first retrieve setting
            Key key = entry.getValue();
            Boolean setting = mAccount.getAccountConfig().getSetting(key);
            if (setting == null) {
                //setting in account doesn't exist in view, should hide view
                continue;
            }

            //get handle on view and set it's toggle
            int switchId = entry.getKey();
            SwitchCompat switchCompat = (SwitchCompat) mView.findViewById(switchId);
            switchCompat.setChecked(setting);
        }
    }

    @OnClick({
            R.id.wine_identification_switch,
            R.id.tagged_on_a_wine_switch,
            R.id.comment_on_your_wine_switch,
            R.id.respond_to_comment_switch,
            R.id.like_your_wine_switch,
            R.id.following_you_switch,
            R.id.friend_joined_delectable_switch
    })
    protected void onSwitchChanged(SwitchCompat switchCompat) {
        Log.d(TAG, "switchClicked");
        Log.d(TAG, "switchChecked?: " + switchCompat.isChecked());

        AccountConfig.Key key = VIEW_KEY_MAP.get(switchCompat.getId());
        if (key != null) {
            updateAccountSettings(key, switchCompat.isChecked());
        }
    }

    private void updateAccountSettings(AccountConfig.Key key, boolean setting) {
        mAccountController.updateSetting(key, setting);
    }

    public void onEventMainThread(UpdatedSettingEvent event) {
        if (!event.isSuccessful()) {
            showToastError(event.getErrorMessage());
        }

        mAccount.getAccountConfig().setSetting(event.getKey(), event.getSetting());
    }
}

