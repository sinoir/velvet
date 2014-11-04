package com.delectable.mobile.api.cache;

import com.delectable.mobile.api.models.BaseWine;

import java.util.HashMap;

public class BaseWineModel {

    private static final String TAG = BaseWineModel.class.getSimpleName();


    public static final HashMap<String, BaseWine> mMap = new HashMap<String, BaseWine>();


    public BaseWine getBaseWine(String id) {
        return mMap.get(id);
    }

    public void saveBaseWine(BaseWine baseWine) {
        mMap.put(baseWine.getId(), baseWine);
    }

    public static void clear() {
        mMap.clear();
    }
}
