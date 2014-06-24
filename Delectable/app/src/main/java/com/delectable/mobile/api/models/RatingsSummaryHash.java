package com.delectable.mobile.api.models;

public class RatingsSummaryHash {

    Ratings all;

    Ratings pro;

    public int getAllCount() {
        return all != null ? all.count : 0;
    }

    public int getProCount() {
        return pro != null ? pro.count : 0;
    }

    public double getAllAvg() {
        return all != null ? all.avg : 0;
    }

    public double getProAvg() {
        return pro != null ? pro.avg : 0;
    }

    public Ratings getAll() {
        return all;
    }

    public void setAll(Ratings all) {
        this.all = all;
    }

    public Ratings getPro() {
        return pro;
    }

    public void setPro(Ratings pro) {
        this.pro = pro;
    }

    @Override
    public String toString() {
        return "RatingsSummaryHash{" +
                "all=" + all +
                ", pro=" + pro +
                '}';
    }

    private class Ratings {

        Integer count;

        Double avg;

        @Override
        public String toString() {
            return "Ratings{" +
                    "count=" + count +
                    ", avg=" + avg +
                    '}';
        }
    }
}
