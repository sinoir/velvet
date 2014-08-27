package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.events.BaseEvent;

public class UpdatedSettingEvent extends BaseEvent {

    private AccountConfig.Key mKey;

    private boolean mSetting;

    public UpdatedSettingEvent(String errorMessage) {
        super(errorMessage);
    }

    public UpdatedSettingEvent(AccountConfig.Key key, boolean setting) {
        super(true);
        mKey = key;
        mSetting = setting;
    }

    public AccountConfig.Key getKey() {
        return mKey;
    }

    public boolean getSetting() {
        return mSetting;
    }
}
