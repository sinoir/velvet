package com.delectable.mobile.model.api;

public class BaseResponse {

    public boolean e_tag_match;

    private String e_tag;

    private String context;

    private boolean success;

    private boolean invalidate;

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

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

}
