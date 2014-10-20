package com.delectable.mobile.api.cache;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.AccountProfile;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class AccountModel {

    private static final String VERSION = "v1_";

    private static final String KEY_PREFIX = "account_";

    private static final String MINIMAL = KEY_PREFIX + "minimal_" + VERSION;

    private static final String PROFILE = KEY_PREFIX + "profile_" + VERSION;

    @Inject
    protected CacheManager mCache;

    public AccountProfile getAccount(String id) {
        Type type = new TypeToken<AccountProfile>() {
        }.getType();
        return (AccountProfile) mCache.get(PROFILE + id, AccountProfile.class, type);
    }

    public void saveAccount(AccountProfile account) {
        mCache.put(PROFILE + account.getId(), account);
    }

    public AccountMinimal getAccountMinimal(String id) {
        Type type = new TypeToken<AccountMinimal>() {
        }.getType();
        return (AccountMinimal) mCache.get(MINIMAL + id, AccountMinimal.class, type);
    }

    public void saveAccountMinimal(AccountMinimal account) {
        mCache.put(MINIMAL + account.getId(), account);
    }

}
