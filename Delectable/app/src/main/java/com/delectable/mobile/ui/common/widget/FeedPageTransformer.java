package com.delectable.mobile.ui.common.widget;


import android.support.v4.view.ViewPager;
import android.view.View;

public class FeedPageTransformer implements ViewPager.PageTransformer {

    private static final float MIN_SCALE = 0.75f;

    private static final float MIN_ALPHA = 0.60f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//                view.setScaleX(scaleFactor);
//                view.setScaleY(scaleFactor);

            // Fade the page out.
//            view.setAlpha(1 + position);
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            // Counteract the default slide transition with parallax effect
            view.setTranslationX(pageWidth * -position / 2);


        } else if (position <= 1) { // (0,1]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
//                view.setScaleX(1);
//                view.setScaleY(1);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}