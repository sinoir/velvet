package com.delectable.mobile.util;


import java.util.Date;

public class DateHelperUtil {

    public static long longDateFromDouble(double dateDouble) {
        // Takes a double and convert it to long:
        // 1398530839.6009998 to 1398530839600
        // 1: 1398530839.6009998 to 1398530839 (firstPart)
        // 2: 1398530839.6009998 to 600 (lastPart)
        // 3: 1398530839 * 1000 + 600 = 1398530839600

        long firstPart = (long) dateDouble;
        double decimal = dateDouble - firstPart;
        long lastPart = (long) (decimal * 1000.0f);
        return (firstPart * 1000) + lastPart;
    }

    /**
     * API uses floats for dates, convert a Date object to a double
     *
     * @param date - Date Object
     * @return - Double format
     */
    public static double doubleFromDate(Date date) {
        long initialDate = date.getTime();
        long firstPart = initialDate / 1000l;
        double decimalPart = (initialDate % 1000l) / 1000.0f;
        return firstPart + decimalPart;
    }

    public static Date dateFromDouble(double dateDouble) {
        return new Date(longDateFromDouble(dateDouble));
    }
}
