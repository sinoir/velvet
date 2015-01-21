package com.delectable.mobile.api.endpointmodels.hashtags;

import com.delectable.mobile.api.endpointmodels.BaseRequest;
import com.delectable.mobile.api.endpointmodels.SearchRequest;

public class SearchHashtagsRequest extends BaseRequest {

    private Payload payload;

    public SearchHashtagsRequest(String q, int offset, int limit) {
        this.payload = new Payload(q, offset, limit);
    }

    public SearchHashtagsRequest(String q, int offset, int limit, boolean capture_counts,
            String wine_profile_id, String base_wine_id, String capture_id,
            String pending_capture_id) {
        this.payload = new Payload(q, offset, limit, capture_counts, wine_profile_id, base_wine_id, capture_id, pending_capture_id);
    }

    public static class Payload extends SearchRequest.Payload {

        private boolean capture_counts = false;

        private String wine_profile_id;

        private String base_wine_id;

        private String capture_id;

        private String pending_capture_id;

        /**
         * See {@link #Payload(String, int, int, boolean, String, String, String, String)}
         */
        public Payload(String q, int offset, int limit) {
            this(q, offset, limit, false, null, null, null, null);
        }

        /**
         * One of {@code wine_profile_id}, {@code base_wine_id}, {@code capture_id}, and {@code
         * pending_capture_id} can be provided to help improve the relevancy of the results.
         *
         * @param q                  query
         * @param offset             used for pagination
         * @param limit              number of results to return
         * @param capture_counts     default is false. Note there is a small performance penalty
         *                           when using this flag, so it's best to only use it when
         *                           necessary.
         * @param wine_profile_id    optional
         * @param base_wine_id       optional
         * @param capture_id         optional
         * @param pending_capture_id optional
         */
        public Payload(String q, int offset, int limit, boolean capture_counts,
                String wine_profile_id, String base_wine_id, String capture_id,
                String pending_capture_id) {
            super(q, offset, limit);
            this.capture_counts = capture_counts;
            this.wine_profile_id = wine_profile_id;
            this.base_wine_id = base_wine_id;
            this.capture_id = capture_id;
            this.pending_capture_id = pending_capture_id;
        }
    }

}
