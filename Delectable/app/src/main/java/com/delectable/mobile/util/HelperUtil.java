package com.delectable.mobile.util;

import org.apache.commons.lang3.text.WordUtils;

import android.location.Location;
import android.net.Uri;

import java.util.Map;

public class HelperUtil {

    public static String snakeCaseToCamelCase(String snakeCasedString) {
        String capitalizedCamelString = WordUtils.capitalize(snakeCasedString, '_')
                .replace("_", "");
        String firstLetterLowered = ("" + capitalizedCamelString.charAt(0)).toLowerCase();
        firstLetterLowered += capitalizedCamelString.substring(1, capitalizedCamelString.length());
        return firstLetterLowered;
    }

    public static String getterMethodNameFromFieldName(String fieldName) {
        return snakeCaseToCamelCase("get_" + fieldName);
    }

    /**
     * Build GET URL with Parameters
     *
     * @param fullUrl   - Base Url , such as http://api.delectable.com
     * @param paramsMap - HashMap of Parameters, that will be used to create ?field=value&... etc.
     *                  Null value will just return teh fullUrl
     * @return Built URL for get request
     */
    public static String buildUrlWithParameters(String fullUrl, Map<String, String> paramsMap) {
        if (paramsMap == null) {
            return fullUrl;
        }
        Uri.Builder uriBuilder = new Uri.Builder();
        for (String key : paramsMap.keySet()) {
            uriBuilder.appendQueryParameter(key, paramsMap.get(key));
        }
        Uri builtUri = uriBuilder.build();
        return fullUrl + builtUri.toString();
    }

    /**
     * Get the latitude and longitude from the Location object returned by Location Services.
     *
     * @param currentLocation A Location object containing the current location
     * @return The latitude and longitude of the current location separated by a comma, or empty
     * string if no location is available.
     */
    public static String getLatLngFromLocation(Location currentLocation) {
        // If the location is valid
        if (currentLocation != null) {
            // Return the latitude and longitude as strings
            return currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        } else {
            // Otherwise, return the empty string
            return "";
        }
    }
}
