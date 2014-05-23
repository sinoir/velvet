package com.delectable.mobile;

/**
 * Created by abednarek on 5/21/14.
 */
public class Config {

    public static class ServerInfo {

        public static final String SERVER_MOBILE_URL = "https://mobile-api.delectable.com";

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
