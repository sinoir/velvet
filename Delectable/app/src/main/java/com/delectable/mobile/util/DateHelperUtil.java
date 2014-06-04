package com.delectable.mobile.util;

import java.sql.Date;

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

    public static Date dateFromDouble(double dateDouble) {
        return new Date(longDateFromDouble(dateDouble));
    }
}
