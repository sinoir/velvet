package com.delectable.mobile.api.endpointmodels;

public class SearchRequest extends BaseRequest {

    private Payload payload;

    public SearchRequest(String q, int offset, int limit) {
        this.payload = new Payload(q, offset, limit);
    }

    public static class Payload {

        private String q;

        private int offset;

        private int limit;

        public Payload(String q, int offset, int limit) {
            this.q = q;
            this.offset = offset;
            this.limit = limit;
        }
    }
}
