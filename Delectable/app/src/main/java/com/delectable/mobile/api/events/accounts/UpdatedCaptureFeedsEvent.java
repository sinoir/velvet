package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.CaptureFeed;

import java.util.List;

public class UpdatedCaptureFeedsEvent extends BaseEvent {

    private List<CaptureFeed> mCaptureFeeds;

    public UpdatedCaptureFeedsEvent(List<CaptureFeed> captureFeeds) {
        super(true);
        mCaptureFeeds = captureFeeds;
    }

    public UpdatedCaptureFeedsEvent(List<CaptureFeed> captureFeeds, String errorMessage) {
        super(errorMessage);
        mCaptureFeeds = captureFeeds;
    }

    public List<CaptureFeed> getCaptureFeeds() {
        return mCaptureFeeds;
    }

}
