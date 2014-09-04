package com.delectable.mobile.data;

import com.google.gson.reflect.TypeToken;

import com.delectable.mobile.api.models.BaseWine;
import com.iainconnor.objectcache.CacheManager;

import java.lang.reflect.Type;

import javax.inject.Inject;

public class BaseWineModel {

    private static final String TAG = BaseWineModel.class.getSimpleName();

    private static final String KEY_PREFIX = "base_wines_";

    @Inject
    CacheManager mCache;

    private Type mWineType = new TypeToken<BaseWine>() {
    }.getType();

    public BaseWine getBaseWine(String id) {
        return (BaseWine) mCache.get(KEY_PREFIX + id, BaseWine.class, mWineType);
    }

    public void saveBaseWine(BaseWine baseWine) {
        mCache.put(KEY_PREFIX + baseWine.getId(), baseWine);
    }
}
