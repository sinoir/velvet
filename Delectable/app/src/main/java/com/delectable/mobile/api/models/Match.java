package com.delectable.mobile.api.models;

public class Match {

    private double score;

    private BaseWine base_wine;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public BaseWine getBaseWine() {
        return base_wine;
    }

    public void setBaseWine(BaseWine base_wine) {
        this.base_wine = base_wine;
    }

    @Override
    public String toString() {
        return "Match{" +
                "score=" + score +
                ", base_wine=" + base_wine +
                '}';
    }
}
