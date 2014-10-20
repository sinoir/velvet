package com.delectable.mobile.api.endpointmodels.accounts;

public enum CapturesContext {
    MINIMAL("minimal"),
    DETAILS("details");

    private String mLabel;

    private CapturesContext(String label) {

        mLabel = label;
    }

    public String toString() {
        return mLabel;
    }
}
