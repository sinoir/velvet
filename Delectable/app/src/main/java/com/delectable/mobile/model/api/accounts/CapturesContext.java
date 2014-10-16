package com.delectable.mobile.model.api.accounts;

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