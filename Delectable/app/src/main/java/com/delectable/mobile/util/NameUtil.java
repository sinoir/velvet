package com.delectable.mobile.util;

import android.text.TextUtils;

import java.util.Arrays;

public class NameUtil {

    public static final int FIRST_NAME = 0;

    public static final int LAST_NAME = 1;

    /**
     * @return Returns a String[] where the first name is index 0, and the last name is index 1.
     */
    public static String[] getSplitName(String fullName) {

        String[] name = new String[2];

        //purge new lines
        fullName = fullName.replaceAll("\\r\\n|\\r|\\n", "");

        if (fullName == null || fullName.equals("")) {
            name[FIRST_NAME] = "";
            name[LAST_NAME] = "";
            return name;
        }

        //split name by whitespace
        String[] splitName = fullName.split("\\s+");

        //mononym
        if (splitName.length == 1) {
            name[FIRST_NAME] = fullName;
            //server thing, it doesn't take empty strings as a name, but it does white space
            name[LAST_NAME] = " ";
            return name;
        }

        //rebuild first name(s), excluding last string in splitname
        String[] firstNames = Arrays.copyOfRange(splitName, 0, splitName.length - 1);
        name[FIRST_NAME] = TextUtils.join(" ", firstNames);
        name[LAST_NAME] = splitName[splitName.length - 1];

        return name;
    }

}
