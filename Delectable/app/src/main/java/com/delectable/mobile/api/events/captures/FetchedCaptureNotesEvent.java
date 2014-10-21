package com.delectable.mobile.api.events.captures;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.BaseListingResponse;
import com.delectable.mobile.api.models.CaptureNote;

public class FetchedCaptureNotesEvent extends BaseEvent {

    private BaseListingResponse<CaptureNote> mListingResponse;

    public FetchedCaptureNotesEvent(BaseListingResponse<CaptureNote> listingResponse) {
        super(true);
        mListingResponse = listingResponse;
    }

    public FetchedCaptureNotesEvent(String errorMessage) {
        super(errorMessage);
    }

    public BaseListingResponse<CaptureNote> getListingResponse() {
        return mListingResponse;
    }
}
