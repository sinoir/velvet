package com.delectable.mobile.util;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by abednarek on 5/21/14.
 */
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
}
