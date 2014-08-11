package com.delectable.mobile.data;

import com.delectable.mobile.BuildConfig;
import com.iainconnor.objectcache.CacheManager;
import com.iainconnor.objectcache.DiskCache;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Cache {

    private static final int CACHE_SIZE_BYTES = 1024 * 1024 * 10;

    private static CacheManager sCacheManager;
    private static DiskCache sDiskCache;

    private static final String TAG = Cache.class.getSimpleName();

    private Cache() {
    }

    public static boolean init(Context context) {

        String cachePath = context.getCacheDir().getPath();
        File cacheFile = new File(cachePath + File.separator + BuildConfig.PACKAGE_NAME);

        try {
            sDiskCache = new DiskCache(cacheFile, BuildConfig.VERSION_CODE, CACHE_SIZE_BYTES);
            sCacheManager = CacheManager.getInstance(sDiskCache);
        } catch (IOException e) {
            Log.e(TAG, "cache init failed: " + e.getMessage());
            return false;
        }

        return true;
    }

    public static CacheManager getCacheManager() {
        return sCacheManager;
    }

    public static void clearCache() {
        try {
            sDiskCache.clearCache();
        } catch (IOException e) {
            Log.e(TAG, "cache clearing failed: " + e.getMessage());
        }
    }

}
