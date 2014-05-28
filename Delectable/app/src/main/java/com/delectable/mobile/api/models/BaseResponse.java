package com.delectable.mobile.api.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

public abstract class BaseResponse {

    String context;

    String e_tag;

    public static <T extends BaseResponse> T buildFromJson(JSONObject json, Class<T> tClass) {
        return buildFromJson(json, tClass, false);
    }

    public static <T extends BaseResponse> T buildFromJsonForExposedObjects(JSONObject json,
            Class<T> tClass) {
        return buildFromJson(json, tClass, true);
    }

    public static <T extends BaseResponse> T buildFromJson(JSONObject json, Class<T> tClass,
            boolean shouldUseExposeAnnotations) {
        GsonBuilder builder = new GsonBuilder();
        if (shouldUseExposeAnnotations) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        Gson gson = builder.create();
        String jsonString = "";
        jsonString = json.toString();
        return gson.fromJson(jsonString, tClass);
    }

    public abstract BaseResponse buildFromJson(JSONObject jsonObj);

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
        return "BaseResponse{" +
                "context='" + context + '\'' +
                ", e_tag='" + e_tag + '\'' +
                '}';
    }
}
