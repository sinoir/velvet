package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.AccountProfile;

import java.util.HashMap;

public class AccountModel {

    private static final String VERSION = "v1_";

    private static final String KEY_PREFIX = "account_";

    private static final String MINIMAL = KEY_PREFIX + "minimal_" + VERSION;

    private static final String PROFILE = KEY_PREFIX + "profile_" + VERSION;

    private static final HashMap<String, AccountProfile> mAccountProfileMap
            = new HashMap<String, AccountProfile>();

    private static final HashMap<String, AccountMinimal> mAccountMinimalMap
            = new HashMap<String, AccountMinimal>();

    public AccountProfile getAccount(String id) {
        String key = PROFILE + id;
        return mAccountProfileMap.get(key);
    }

    public void saveAccount(AccountProfile account) {
        String key = PROFILE + account.getId();
        mAccountProfileMap.put(key, account);
    }

    public AccountMinimal getAccountMinimal(String id) {
        String key = MINIMAL + id;
        return mAccountMinimalMap.get(key);
    }

    public void saveAccountMinimal(AccountMinimal account) {
        String key = MINIMAL + account.getId();
        mAccountMinimalMap.put(key, account);
    }

    public static void clear() {
        mAccountProfileMap.clear();
        mAccountMinimalMap.clear();
    }

}
