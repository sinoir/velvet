package com.delectable.mobile.api;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by abednarek on 5/22/14.
 */
public class RequestError {

    int code;

    String message;

    public static RequestError buildFromJson(JSONObject json) {
        Gson gson = new Gson();
        String jsonString = "";
        if (json.opt("error") != null) {
            jsonString = json.opt("error").toString();
        } else {
            jsonString = json.opt("payload").toString();
        }

        return gson.fromJson(jsonString, RequestError.class);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
