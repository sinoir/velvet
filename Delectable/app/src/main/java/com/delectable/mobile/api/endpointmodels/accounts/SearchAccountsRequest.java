package com.delectable.mobile.api.endpointmodels.accounts;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.SearchRequest;

public class SearchAccountsRequest extends BaseRequest {

    private Payload payload;

    /**
     * See {@link Payload#Payload(String, int, int, boolean, String)}
     */
    public SearchAccountsRequest(String q, int offset, int limit) {
        this.payload = new Payload(q, offset, limit);
    }

    /**
     * See {@link Payload#Payload(String, int, int, boolean, String)}
     */
    public SearchAccountsRequest(String q, int offset, int limit, boolean contextual, String captureId) {
        this.payload = new Payload(q, offset, limit, contextual, captureId);
    }

    public static class Payload extends SearchRequest.Payload {

        private boolean contextual = false;

        private String capture_id;

        /**
         * See {@link #Payload(String, int, int, boolean, String)}
         */
        public Payload(String q, int offset, int limit) {
            this(q, offset, limit, false, null);
        }

        /**
         * @param q          query
         * @param offset     used for pagination
         * @param limit      number of results to return
         * @param contextual If {@code true}, Delectafriends and follows will be scored more highly
         * @param capture_id If provided, it will be used to improve relevancy of the results.
         */
        public Payload(String q, int offset, int limit, boolean contextual, String capture_id) {
            super(q, offset, limit);
            this.contextual = contextual;
            this.capture_id = capture_id;
        }
    }

}
