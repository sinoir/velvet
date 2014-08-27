package com.delectable.mobile.events.accounts;

import com.delectable.mobile.events.BaseEvent;

public class UpdatedProfileEvent extends BaseEvent {

    private String mFname;

    private String mLname;

    private String mUrl;

    private String mBio;

    public UpdatedProfileEvent(String errorMessage) {
        super(errorMessage);
    }

    public UpdatedProfileEvent(String fname, String lname, String url, String bio) {
        super(true);
        mFname = fname;
        mLname = lname;
        mUrl = url;
        mBio = bio;
    }

    public String getFname() {
        return mFname;
    }

    public String getLname() {
        return mLname;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getBio() {
        return mBio;
    }
}
