package com.delectable.mobile.model.api.accounts;

import com.delectable.mobile.api.models.AccountConfig;
import com.delectable.mobile.model.api.BaseRequest;

public class AccountsUpdateSettingRequest extends BaseRequest {

    private Payload payload;

    public AccountsUpdateSettingRequest(AccountConfig.Key key, boolean setting) {
        this.payload = new Payload(key.getName(), setting);
    }

    public static class Payload {

        private String key;

        private boolean setting;

        public Payload(String key, boolean setting) {
            this.key = key;
            this.setting = setting;
        }
    }
}
