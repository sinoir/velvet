package com.delectable.mobile.model.api;

public class BaseRequest {

    private String context;
    private String e_tag;

    private final String sessionType = "mobile";
    private String sessionKey;
    private String sessionToken;

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

}
