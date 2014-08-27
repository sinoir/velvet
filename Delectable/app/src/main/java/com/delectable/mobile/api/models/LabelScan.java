package com.delectable.mobile.api.models;

import java.util.List;

public class LabelScan {

    String id;

    List<BaseWine> matches;

    public String getId() {
        return id;
    }

    public List<BaseWine> getMatches() {
        return matches;
    }

    @Override
    public String toString() {
        return "LabelScan{" +
                "id='" + id + '\'' +
                ", matches=" + matches +
                '}';
    }
}
