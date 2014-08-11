package com.delectable.mobile;

public class Config {

    public static final String API_VERSION = "/v2";
    public static final String DEFAULT_SESSION_TYPE = "mobile";

    public static class ServerInfo {

        public static final String SERVER_MOBILE_URL = "http://mobile-api.delectable.com";

        public static final String SERVER_STAGING_URL = "https://staging-api.delectable.com";

        public static final String SERVER_PROD_URL = "https://api.delectable.com";

        // TODO: Build SharedPrefs for storing pre selected server for testing...
        public static final String[] SERVER_URLS = new String[]{
                SERVER_MOBILE_URL,
                SERVER_STAGING_URL,
                SERVER_PROD_URL,
        };
    }
}
