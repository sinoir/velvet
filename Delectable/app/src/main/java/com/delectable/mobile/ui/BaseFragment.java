package com.delectable.mobile.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BaseFragment extends Fragment {

    private ActionBar mActionBar;

    private ImageView mCustomHomeImageView;

    private boolean mIsUsingCustomActionbarView = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = getActivity().getActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        toggleCustomActionBar();
    }

    public void launchNextFragment(BaseFragment fragment) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.replaceWithFragment(fragment);
    }


    /**
     * * Override home icon with custom view with click listener
     *
     * Note: Having a title will push this view to the right of the title.
     *
     * @param resId    - Drawable resource
     * @param listener (Optional) - OnClick for the custom view
     */
    public void overrideHomeIcon(int resId, View.OnClickListener listener) {
        // TODO: Find if there's a better alternative: having a title will push this custom view to the right
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        // Custom Image View
        mCustomHomeImageView = new ImageView(getActivity());
        int horizontalPadding = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, metrics);
        mCustomHomeImageView.setPadding(horizontalPadding, 0, horizontalPadding, 0);
        mCustomHomeImageView.setImageResource(resId);

        if (listener != null) {
            mCustomHomeImageView.setOnClickListener(listener);
        }

        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setCustomView(mCustomHomeImageView, params);
        mIsUsingCustomActionbarView = true;
    }

    private void toggleCustomActionBar() {
        if (mIsUsingCustomActionbarView && mCustomHomeImageView != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setCustomView(mCustomHomeImageView);
        } else if (mActionBar.getCustomView() != null) {
            mActionBar.setDisplayShowCustomEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
