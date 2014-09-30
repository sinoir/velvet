package com.delectable.mobile.model.api.captures;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureNote;
import com.delectable.mobile.api.models.ListingResponse;
import com.delectable.mobile.model.api.BaseResponse;

public class CaptureNotesResponse extends BaseResponse {

    private ListingResponse<CaptureNote> payload;

    public ListingResponse<CaptureNote> getPayload() {
        return payload;
    }
}
