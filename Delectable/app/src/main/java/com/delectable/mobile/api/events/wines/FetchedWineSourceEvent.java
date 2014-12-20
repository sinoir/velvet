package com.delectable.mobile.api.events.wines;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.util.ErrorUtil;

import android.support.annotation.Nullable;

/**
 * It's possible to fetch wine source from a capture details list and as well as the wine profile
 * screen. When fetching from a captures list, we provide the capture id as well, so that the
 * fragment knows which capture to update in the list.
 */
public class FetchedWineSourceEvent extends BaseEvent {

    private String mWineId;

    private String mCaptureId;

    private CaptureDetails mCaptureDetails;

    public FetchedWineSourceEvent(@Nullable CaptureDetails capture, String wineId) {
        super(true);
        mWineId = wineId;
        mCaptureDetails = capture;
        if (mCaptureDetails != null) {
            mCaptureId = mCaptureDetails.getId();
        }
    }

    public FetchedWineSourceEvent(String errorMessage, ErrorUtil errorCode,
            @Nullable String captureId, String wineId) {
        super(errorMessage, errorCode);
        mWineId = wineId;
        mCaptureId = captureId;
    }

    public FetchedWineSourceEvent(String errorMessage, ErrorUtil errorCode,
            @Nullable CaptureDetails capture, String wineId) {
        super(errorMessage, errorCode);
        mWineId = wineId;
        mCaptureDetails = capture;
        if (mCaptureDetails != null) {
            mCaptureId = mCaptureDetails.getId();
        }
    }

    public String getWineId() {
        return mWineId;
    }

    public
    @Nullable
    String getCaptureId() {
        return mCaptureId;
    }

    public
    @Nullable
    CaptureDetails getCaptureDetails() {
        return mCaptureDetails;
    }
}
