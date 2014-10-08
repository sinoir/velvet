package com.delectable.mobile.api.util;

import java.util.HashMap;

public enum ErrorUtil {
    TEST(0),
    ALREADY_EXISTS(409),
    PROCESS_CANCELLED(450),
    INTERNAL_SERVER_ERROR(500),
    TIMEOUT(503),
    MISSING_PARAMETERS(1000),
    MALFORMED_PARAMETERS(1500),
    UNKNOWN_CONTEXT(1250),
    INVALID_ADDRESS(1510),
    INVALID_PAYMENT_INFO(1520),
    INVALID_QUANTITY(1530),
    INVALID_SIGNATURE(1540),
    ASSET_NOT_FOUND(2000),
    ASSET_INVALID(2500),
    TOO_MANY_ASSETS(2750),
    VALIDATION_FAILED(3000),
    UNAUTHORIZED(4000),
    UNAUTHORIZED_BLANK(4001),
    INVALID_SESSION_TYPE(4002),
    INVALID_CREDENTIALS(4100),
    SESSION_DOES_NOT_EXIST(4101),
    INVALID_SESSION_TOKEN(4102),
    EXTERNAL_SERVICE_ERROR(6000),
    AMAZON_ERROR(6100),
    FACEBOOK_ERROR(6200),
    FACEBOOK_ALREADY_TAKEN(6205),
    FACEBOOK_UNAUTHORIZED(6210),
    FACEBOOK_AUTH_NOT_FOUND(6211),
    TWITTER_ERROR(6300),
    TWITTER_UNAUTHORIZED(6310),
    TWITTER_AUTH_NOT_FOUND(6311),
    STRIPE_ERROR(6400),
    FOURSQUARE_ERROR(6500),
    FOURSQUARE_VENUE_NOT_FOUND(6504),
    TRANSACTION_FAILED(6900),
    UNKNOWN_ERROR(9000),
    DEPRECATED_ENDPOINT(10000),
    OAUTH_ERROR(11000),
    OAUTH_IDENTITY_NOT_FOUND(11100),
    OAUTH_CLIENT_NOT_FOUND(11200),
    OAUTH_CLIENT_UNAUTHORIZED(11205),
    OAUTH_CLIENT_REDIRECT_URI_MISMATCH(11210),
    OAUTH_AUTH_REQUEST_NOT_FOUND(11300),
    OAUTH_AUTH_REQUEST_INVALID(11305),
    OAUTH_GRANT_NOT_FOUND(11400),
    OAUTH_GRANT_UNAUTHORIZED(11405),
    OAUTH_GRANT_EXPIRED(11410),
    UNDOCUMENTED_ERROR_CODE(-1),
    // Local App Codes
    NO_NETWORK_ERROR(50000);

    private static final HashMap<Integer, ErrorUtil> ERRORS_BY_CODE
            = new HashMap<Integer, ErrorUtil>();

    static {
        for (ErrorUtil error : values()) {
            ERRORS_BY_CODE.put(error.getCode(), error);
        }
    }

    private int mCode;

    private ErrorUtil(int code) {
        mCode = code;
    }

    public static ErrorUtil valueOfCode(int code) {
        ErrorUtil error = ERRORS_BY_CODE.get(code);
        if (error != null) {
            return error;
        }
        return UNDOCUMENTED_ERROR_CODE;
    }

    public int getCode() {
        return mCode;
    }

}
