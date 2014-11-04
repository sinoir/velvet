package com.delectable.mobile.api.endpointmodels.wineprofiles;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

/**
 * Retrieves pricing information for the Wine Profile
 */
public class WineProfilesSourceRequest extends BaseRequest {

    private Payload payload;

    public WineProfilesSourceRequest(String id, String state) {
        super();
        this.payload = new Payload(id, state);
    }

    public static class Payload {

        private String id;

        private String state;

        public Payload(String id, String state) {
            this.id = id;
            this.state = state;
        }
    }

}
