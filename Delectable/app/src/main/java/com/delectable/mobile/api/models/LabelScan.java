package com.delectable.mobile.api.models;

import java.util.ArrayList;
import java.util.List;

public class LabelScan {

    private String id;

    private ArrayList<Match> matches;

    public String getId() {
        return id;
    }

    public List<BaseWine> getBaseWineMatches() {
        if (matches == null) {
            return null;
        }
        ArrayList<BaseWine> wineMatches = new ArrayList<BaseWine>();
        for (Match match : matches) {
            wineMatches.add(match.getBaseWine());
        }
        return wineMatches;
    }

    @Override
    public String toString() {
        return "LabelScan{" +
                "id='" + id + '\'' +
                ", matches=" + matches +
                '}';
    }
}
