package com.delectable.mobile.events.accounts;

import com.delectable.mobile.api.models.PhotoHash;
import com.delectable.mobile.events.BaseEvent;

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
