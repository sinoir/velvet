package com.delectable.mobile.api.endpointmodels.captures;

import com.delectable.mobile.api.endpointmodels.BaseRequest;

/**
 * Retrieves a list of captures from the requested list
 */
public class CapturesListRequest extends BaseRequest {

    private Payload payload;

    public CapturesListRequest(String list_key, String list_id, String before, String after) {
        payload = new Payload(list_key, list_id, before, after);
    }

    public CapturesListRequest(String list_key, String before, String after) {
        payload = new Payload(list_key, before, after);
    }

    public CapturesListRequest(String list_key) {
        payload = new Payload(list_key);
    }

    public static class Payload {

        private String list_key;

        private String list_id;

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

        public Payload(String list_key, String list_id, String before, String after) {
            this.list_key = list_key;
            this.list_id = list_id;
            this.before = before;
            this.after = after;
        }
    }

}
