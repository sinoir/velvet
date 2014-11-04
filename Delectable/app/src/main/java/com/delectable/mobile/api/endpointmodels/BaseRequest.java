package com.delectable.mobile.api.endpointmodels;

import com.delectable.mobile.Config;

public class BaseRequest {

    private final String sessionType = Config.DEFAULT_SESSION_TYPE;

    private String context;

    private String e_tag;

    private String sessionKey;

    private String sessionToken;

    public BaseRequest() {

    }

    public BaseRequest(String context) {
        this(context, null);
    }

    public BaseRequest(String context, String e_tag) {
        this.context = context;
        this.e_tag = e_tag;
    }

    public void authenticate(String sessionKey, String sessionToken) {
        this.sessionKey = sessionKey;
        this.sessionToken = sessionToken;
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
}
