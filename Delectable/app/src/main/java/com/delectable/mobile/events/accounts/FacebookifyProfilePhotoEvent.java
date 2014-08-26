package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.AccountMinimal;
import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.events.BaseEvent;

import java.util.ArrayList;

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
