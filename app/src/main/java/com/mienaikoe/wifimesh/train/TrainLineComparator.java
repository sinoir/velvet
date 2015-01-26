package com.mienaikoe.wifimesh.train;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jesse on 1/25/2015.
 */
public class TrainLineComparator implements Comparator<TrainLine> {

    private static List<String> STATION_ORDER = Arrays.asList(new String[]{
            "A","C","E",
            "B","D","F","M",
            "G",
            "J","Z",
            "L",
            "N","Q","R","S",
            "1","2","3",
            "4","5","6",
            "7",
            "GS","FS","SI","H"
    });

    @Override
    public int compare(TrainLine lhs, TrainLine rhs) {
        if (lhs == null) {
            if (rhs == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (rhs == null) {
            return 1;
        } else {
            return STATION_ORDER.indexOf(lhs.getName()) - STATION_ORDER.indexOf(rhs.getName());
        }
    }

}
