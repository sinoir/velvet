package com.delectable.mobile.api.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.lang.reflect.Type;

public abstract class BaseResponse {

    String context;

    String e_tag;

    // If a subclass uses generic T, we must specify a TypeToken before serializing/deserializing the gson
    // For example, MyClass<T>
    protected Type mClassType;

    public static <T extends BaseResponse> T buildFromJson(JSONObject json, Class<T> tClass) {
        return buildFromJson(json, tClass, false, null);
    }

    public static <T extends BaseResponse> T buildFromJson(JSONObject json, Type classType) {
        return buildFromJson(json, null, false, classType);
    }

    public static <T extends BaseResponse> T buildFromJsonForExposedObjects(JSONObject json,
            Class<T> tClass) {
        return buildFromJson(json, tClass, true, null);
    }

    public static <T extends BaseResponse> T buildFromJson(JSONObject json, Class<T> tClass,
            boolean shouldUseExposeAnnotations, Type classType) {
        GsonBuilder builder = new GsonBuilder();
        T result;
        if (shouldUseExposeAnnotations) {
            builder.excludeFieldsWithoutExposeAnnotation();
        }
        Gson gson = builder.create();
        String jsonString = "";
        if (json != null) {
            jsonString = json.toString();
        }

        if (classType != null) {
            result = gson.fromJson(jsonString, classType);
        } else {
            result = gson.fromJson(jsonString, tClass);
        }
        return result;
    }

    public Type getClassType() {
        return mClassType;
    }

    public void setClassType(Type classType) {
        mClassType = classType;
    }

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
