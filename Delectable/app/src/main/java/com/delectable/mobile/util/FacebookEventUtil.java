package com.delectable.mobile.util;

import com.delectable.mobile.api.models.CaptureDetails;
import com.delectable.mobile.api.models.CaptureState;
import com.facebook.AppEventsConstants;
import com.facebook.AppEventsLogger;

import android.content.Context;
import android.os.Bundle;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Used to properly measure Facebook mobile install ad campaigns
 */
public class FacebookEventUtil {

    public static void logRegistration(Context context) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION);
    }

    /**
     * mark that this is the first time user is using the app
     */
    public static void logFirstAppUse(Context context) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ACTIVATED_APP);
    }

    public static void logRateEvent(Context context, CaptureDetails capture) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        String wineName = "";
        CaptureState captureState = CaptureState.getState(capture);
        if (captureState == CaptureState.UNVERIFIED) {
            wineName = capture.getBaseWine().getProducerName() + " " +
                    capture.getBaseWine().getName();
        }
        if (captureState == CaptureState.IDENTIFIED) {
            wineName = capture.getWineProfile().getProducerName() + " " +
                    capture.getWineProfile().getName() + " " +
                    capture.getWineProfile().getVintage();
        }
        //if unidentified or impossibled, we just pass in the empty string

        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, wineName);
        logger.logEvent(AppEventsConstants.EVENT_NAME_RATED, parameters);
    }

    public static void logPurchasePageVisit(Context context, String wineName) {
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, wineName);
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, parameters);
    }

    public static void logPurchase(Context context, int quantity, String price) {
        double priceDouble = Double.valueOf(price);
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle parameters = new Bundle();
        parameters.putInt(AppEventsConstants.EVENT_PARAM_NUM_ITEMS, quantity);
        logger.logPurchase(BigDecimal.valueOf(priceDouble), Currency.getInstance("USD"), parameters);
    }

}
