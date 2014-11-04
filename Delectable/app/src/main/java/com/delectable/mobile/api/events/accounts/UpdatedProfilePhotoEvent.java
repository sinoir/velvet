package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.PhotoHash;

public class UpdatedProfilePhotoEvent extends BaseEvent {

    private PhotoHash mPhoto;

    public UpdatedProfilePhotoEvent(String errorMessage) {
        super(errorMessage);
    }

    public UpdatedProfilePhotoEvent(PhotoHash photo) {
        super(true);
        mPhoto = photo;
    }

    public PhotoHash getPhoto() {
        return mPhoto;
    }

}
