package com.delectable.mobile.events;

public class NavigationEvent {

    /**
     * @see com.delectable.mobile.ui.navigation.widget.NavHeader
     */
    public int itemPosition;

    public NavigationEvent(int itemPosition) {
        this.itemPosition = itemPosition;
    }

}
