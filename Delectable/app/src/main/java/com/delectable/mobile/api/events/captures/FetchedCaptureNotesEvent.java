package com.delectable.mobile.api.events.captures;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;

public class FetchedCaptureNotesEvent extends BaseEvent {

    private ListingResponse<CaptureNote> mListingResponse;

    public FetchedCaptureNotesEvent(ListingResponse<CaptureNote> listingResponse) {
        super(true);
        mListingResponse = listingResponse;
    }

    public FetchedCaptureNotesEvent(String errorMessage) {
        super(errorMessage);
    }

    public ListingResponse<CaptureNote> getListingResponse() {
        return mListingResponse;
    }
}
