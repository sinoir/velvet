package com.delectable.mobile.events.captures;

import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.events.BaseEvent;

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
