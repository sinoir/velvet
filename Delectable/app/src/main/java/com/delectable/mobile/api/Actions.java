package com.delectable.mobile.api;

/**
 * Created by abednarek on 5/21/14.
 */
public class Actions {

    public interface BaseWineActions {

        public static final int A_CONTEXT = 0;
    }

    public interface WineProfileActions {

        public static final int A_CONTEXT = 0;
        public static final int A_WISHLIST = 1;
    }

    public interface WineSourceActions {

        public static final int A_SOURCE = 0;
    }

    public interface WinePurchaseActions {

        public static final int A_PURCHASE = 0;
    }
}
