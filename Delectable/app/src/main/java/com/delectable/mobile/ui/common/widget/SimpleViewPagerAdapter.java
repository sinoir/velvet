package com.delectable.mobile.ui.common.widget;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Simple ViewPager Adapter for use with Custom Views
 */
public class SimpleViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> mViews;

    public SimpleViewPagerAdapter(ArrayList<View> views) {
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mViews.get(position);
        container.removeView(view);
    }
}
