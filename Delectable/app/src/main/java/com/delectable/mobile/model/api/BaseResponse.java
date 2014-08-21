package com.delectable.mobile.model.api;

public class BaseResponse {

    public String e_tag;

    public boolean e_tag_match;

    public String context;

    public boolean success;
    
    public boolean invalidate;

    private Error error;

    public String getETag() {
        return e_tag;
    }

    public boolean isETagMatch() {
        return e_tag_match;
    }

    public String getContext() {
        return context;
    }

    public boolean isSuccess() {
        return success;
    }

    public Error getError() {
        return error;
    }

    public static class Error {

        private int code;

        private String message;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

}
