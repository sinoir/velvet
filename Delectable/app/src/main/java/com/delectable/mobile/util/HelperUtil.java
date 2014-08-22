package com.delectable.mobile.util;

import org.apache.commons.lang3.text.WordUtils;

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
}
