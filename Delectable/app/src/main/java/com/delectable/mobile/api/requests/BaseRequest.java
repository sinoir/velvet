package com.delectable.mobile.api.requests;

import com.delectable.mobile.api.models.BaseResponse;
import com.delectable.mobile.util.HelperUtil;

import org.json.JSONObject;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRequest {

    protected static final String API_VER = "/v2";

    String context;

    String e_tag;

    public JSONObject buildPayload() {
        JSONObject obj = new JSONObject(buildPayloadMap());
        return obj;
    }

    public JSONObject buildMetaParams() {
        JSONObject obj = new JSONObject(buildMetaParamsMap());
        return obj;
    }

    public Map<String, String> buildMetaParamsMap() {
        String[] fields = getMetaParamFields();
        HashMap<String, String> metaParamsMap = new HashMap<String, String>();
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            metaParamsMap.put(field, String.valueOf(valueFromField(field)));
        }
        return metaParamsMap;
    }

    public Map<String, String> buildPayloadMap() {
        String[] fields = getPayloadFields();
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

    /**
     * Meta Params is on the same level as payload fields
     */
    public String[] getMetaParamFields() {
        // Build Payload Fields according to what is set and not set
        ArrayList<String> fieldsList = new ArrayList<String>();
        if (e_tag != null) {
            fieldsList.add("e_tag");
        }
        if (context != null) {
            fieldsList.add("context");
        }
        return fieldsList.toArray(new String[fieldsList.size()]);
    }

    public abstract String[] getPayloadFields();

    public abstract String getResourceUrl();

    public abstract <T extends BaseResponse> T buildResopnseFromJson(JSONObject jsonObject);

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    @Override
    public String toString() {
        return "BaseRequest{" +
                "context='" + context + '\'' +
                ", e_tag='" + e_tag + '\'' +
                '}';
    }
}
