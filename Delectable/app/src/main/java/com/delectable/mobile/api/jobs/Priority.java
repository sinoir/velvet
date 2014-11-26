package com.delectable.mobile.api.jobs;

public enum Priority {

    MIN(1),
    SYNC(2),
    PREFETCH(3),
    UX(4); // user facing

    private int value;

    Priority(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

}
