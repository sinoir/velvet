package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.Account;
import com.delectable.mobile.api.models.AccountProfile;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class AccountModel {

    private static final String KEY_PREFIX = "account_";

    @Inject
    CacheManager mCache;

    private Type mAccountType = new TypeToken<AccountProfile>() {
    }.getType();

    public AccountProfile getAccount(String id) {
        return (AccountProfile) mCache.get(KEY_PREFIX + id, AccountProfile.class, mAccountType);
    }

    public void saveAccount(AccountProfile account) {
        mCache.put(KEY_PREFIX + account.getId(), account);
    }

}
