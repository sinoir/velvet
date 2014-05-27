package com.delectable.mobile.api;

/**
 * Created by abednarek on 5/21/14.
 */
public class Actions {

    public interface RegistrationActions {

        public static final int A_REGISTER = 0;
        public static final int A_LOGIN = 1;
        public static final int A_FACEBOOK = 2;
    }

    public interface SearchActions {

        public static final int A_BASE_WINE_SEARCH = 0;
        public static final int A_ACCOUNT_SEARCH = 1;
    }

    public interface BaseWineActions {

        public static final int A_CONTEXT = 0;
    }

    public interface WineProfileActions {

        public static final int A_CONTEXT = 0;
        public static final int A_WISHLIST = 1;
    }
}
