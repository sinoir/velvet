package com.delectable.mobile.api.models;

import com.delectable.mobile.util.HelperUtil;

import org.json.JSONObject;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abednarek on 5/21/14.
 */
public abstract class Resource {


    protected static final String API_VER = "/v2";

    public JSONObject buildPayloadForAction(int action) {
        JSONObject obj = new JSONObject(buildPayloadMapForAction(action));
        return obj;
    }

    public Map<String, String> buildPayloadMapForAction(int action) {
        String[] fields = getPayloadFieldsForAction(action);
        HashMap<String, String> payloadMap = new HashMap<String, String>();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            payloadMap.put(field, String.valueOf(valueFromField(field)));
        }
        return payloadMap;
    }

    public Object valueFromField(String fieldName) {
        String getterMethodName = HelperUtil.getterMethodNameFromFieldName(fieldName);
        Object value = null;
        try {
            Method method = getClass().getMethod(getterMethodName, null);
            // TODO: Check Object Type if it's type of resource or something specific that we need to parse in a special way
            value = method.invoke(this, null);
        } catch (NoSuchMethodException e) {
            Log.wtf(getClass().getSimpleName(), "Failed? ", e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Log.wtf(getClass().getSimpleName(), "Failed? ", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.wtf(getClass().getSimpleName(), "Failed? ", e);
            e.printStackTrace();
        }
        return value;
    }

    public abstract String[] getPayloadFieldsForAction(int action);

    public abstract String getResourceUrlForAction(int action);

    public abstract String parsePayloadForAction(JSONObject payload, int action);

}
