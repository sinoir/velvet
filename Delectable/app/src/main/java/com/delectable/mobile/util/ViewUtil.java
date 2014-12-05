package com.delectable.mobile.util;

import com.delectable.mobile.App;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ViewUtil {

    public static Point getDisplayDimensions() {
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        return screenSize;
    }

}
