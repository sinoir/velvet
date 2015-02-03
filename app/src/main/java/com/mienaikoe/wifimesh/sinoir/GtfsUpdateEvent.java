package com.mienaikoe.wifimesh.sinoir;

import com.google.transit.realtime.GtfsRealtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hoyinlai on 2/1/2015.
 */
public class GtfsUpdateEvent {
    private List<GtfsRealtime.FeedMessage> mMessages;

    public GtfsUpdateEvent(Collection<GtfsRealtime.FeedMessage> messages) {
        mMessages = new ArrayList<>(messages);
    }

    public List<GtfsRealtime.FeedMessage> getMessages() {
        return mMessages;
    }
}
