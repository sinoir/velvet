package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.events.BaseEvent;

public class FacebookifyProfilePhotoEvent extends BaseEvent {

    private PhotoHash mPhoto;

    public FacebookifyProfilePhotoEvent(String errorMessage) {
        super(errorMessage);
    }

    public FacebookifyProfilePhotoEvent(PhotoHash photo) {
        super(true);
        mPhoto = photo;
    }

    public PhotoHash getPhoto() {
        return mPhoto;
    }

}
