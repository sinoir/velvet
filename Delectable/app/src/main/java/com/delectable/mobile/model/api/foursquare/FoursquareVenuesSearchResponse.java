package com.delectable.mobile.model.api.foursquare;

import com.delectable.mobile.model.api.BaseResponse;

import java.util.List;

public class FoursquareVenuesSearchResponse extends BaseResponse {

    private MetaObject meta;

    private ResponseObject response;

    public MetaObject getMeta() {
        return meta;
    }

    public ResponseObject getResponse() {
        return response;
    }

    @Override
    public boolean isSuccess() {
        return getMeta().getCode() == 200;
    }

    @Override
    public Error getError() {
        if (getMeta().getErrorDetail() != null) {
            return new Error(getMeta().getCode(), getMeta().getErrorDetail());
        }
        return null;
    }

    public static class ResponseObject {

        boolean confident;

        List<FoursquareVenueItem> venues;

        public boolean isConfident() {
            return confident;
        }

        public List<FoursquareVenueItem> getVenues() {
            return venues;
        }
    }

    public static class MetaObject {

        int code;

        String errorDetail;

        public int getCode() {
            return code;
        }

        public String getErrorDetail() {
            return errorDetail;
        }
    }
}
