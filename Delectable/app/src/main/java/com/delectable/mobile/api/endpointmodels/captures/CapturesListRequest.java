package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

/**
 * Retrieves a list of captures from the requested list
 */
public class CapturesListRequest extends BaseRequest {

    private Payload payload;

    public CapturesListRequest(String list_key, String eTag, String before, String after) {
        super(null, eTag);
        payload = new Payload(list_key, before, after);
    }

    public CapturesListRequest(String list_key, String eTag) {
        super(null, eTag);
        payload = new Payload(list_key);
    }

    public static class Payload {

        private String list_key;

        private String before;

        private String after;

        public Payload(String list_key) {
            this.list_key = list_key;
        }

        public Payload(String list_key, String before, String after) {
            this.list_key = list_key;
            this.before = before;
            this.after = after;
        }
    }

}
