package com.delectable.mobile.api.events.ui;


import android.graphics.Rect;

public class InsetsChangedEvent {

    public Rect insets;

    public InsetsChangedEvent(Rect insets) {
        this.insets = insets;
    }

}
