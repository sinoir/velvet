package com.delectable.mobile.events;

import com.delectable.mobile.ui.navigation.widget.NavHeader;

public class NavigationEvent {

    /**
     * @see com.delectable.mobile.ui.navigation.widget.NavHeader
     */
    public int itemPosition;

    public NavigationEvent(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public NavigationEvent() {
        this(NavHeader.NAV_HOME);
    }

}
