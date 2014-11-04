package com.delectable.mobile.api.endpointmodels.accounts;

public enum AccountContext {
    SEARCH("search"),
    MINIMAL("minimal"),
    PROFILE("profile"),
    PRIVATE("private");

    private String mContext;

    private AccountContext(String context) {

        mContext = context;
    }

    public String toString() {
        return mContext;
    }
}
