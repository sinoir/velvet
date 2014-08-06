package com.delectable.mobile.data;

import com.delectable.mobile.model.local.Account;
import com.google.gson.reflect.TypeToken;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class AccountModel {

    private static final String KEY_PREFIX = "account_";

    @Inject
    CacheManager mCache;

    private Type mAccountType = new TypeToken<Account>() {
    }.getType();

    public Account getAccount(String id) {
        return (Account) mCache.get(KEY_PREFIX + id, Account.class, mAccountType);
    }

    public void saveAccount(Account account) {
        mCache.put(KEY_PREFIX + account.getId(), account);
    }

}
