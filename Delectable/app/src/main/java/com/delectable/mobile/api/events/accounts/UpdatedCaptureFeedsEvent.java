package com.delectable.mobile.api.events.accounts;

import com.delectable.mobile.api.events.BaseEvent;
import com.delectable.mobile.api.models.CaptureFeed;

import java.util.List;

public class UpdatedCaptureFeedsEvent extends BaseEvent {

    private List<CaptureFeed> mCaptureFeeds;

    private List<CaptureFeed> mOldCaptureFeeds;

    public UpdatedCaptureFeedsEvent(List<CaptureFeed> captureFeeds,
            List<CaptureFeed> oldCaptureFeeds) {
        super(true);
        mCaptureFeeds = captureFeeds;
        mOldCaptureFeeds = oldCaptureFeeds;
    }

    public UpdatedCaptureFeedsEvent(List<CaptureFeed> captureFeeds,
            List<CaptureFeed> oldCaptureFeeds, String errorMessage) {
        super(errorMessage);
        mCaptureFeeds = captureFeeds;
        mOldCaptureFeeds = oldCaptureFeeds;
    }

    public List<CaptureFeed> getCaptureFeeds() {
        return mCaptureFeeds;
    }

    public List<CaptureFeed> getOldCaptureFeeds() {
        return mOldCaptureFeeds;
    }

}
