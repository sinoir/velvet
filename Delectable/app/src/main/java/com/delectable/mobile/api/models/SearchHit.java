package com.delectable.mobile.api.models;

public class SearchHit<T extends BaseResponse> {

    String type;

    Double score;

    T object;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "SearchHit{" +
                "type='" + type + '\'' +
                ", score=" + score +
                ", object=" + object +
                '}';
    }
}
