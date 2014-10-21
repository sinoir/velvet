package com.delectable.mobile.api.events.captures;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.Listing;
import com.delectable.mobile.api.models.CaptureNote;

public class FetchedCaptureNotesEvent extends BaseEvent {

    private Listing<CaptureNote> mListingResponse;

    public FetchedCaptureNotesEvent(Listing<CaptureNote> listingResponse) {
        super(true);
        mListingResponse = listingResponse;
    }

    public FetchedCaptureNotesEvent(String errorMessage) {
        super(errorMessage);
    }

    public Listing<CaptureNote> getListingResponse() {
        return mListingResponse;
    }
}
