package com.delectable.mobile.ui;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BaseFragment extends Fragment {

    private ActionBar mActionBar;

    private ImageView mCustomHomeImageView;

    private RelativeLayout mCustomActionBarView;

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

    public void showToastError(String error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
        }
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
        ActionBar.LayoutParams customViewpParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams imageViewpParams = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        mCustomActionBarView = new RelativeLayout(getActivity());

        // Custom Image View
        mCustomHomeImageView = new ImageView(getActivity());
        int horizontalPadding = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, metrics);
        mCustomHomeImageView.setPadding(horizontalPadding, 0, horizontalPadding, 0);
        mCustomHomeImageView.setImageResource(resId);

        if (listener != null) {
            mCustomHomeImageView.setOnClickListener(listener);
        }

        mCustomActionBarView.addView(mCustomHomeImageView, imageViewpParams);

        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setCustomView(mCustomActionBarView, customViewpParams);
        mIsUsingCustomActionbarView = true;
    }

    private void toggleCustomActionBar() {
        if (mIsUsingCustomActionbarView && mCustomActionBarView != null) {
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setCustomView(mCustomActionBarView);
        } else if (mActionBar.getCustomView() != null) {
            mActionBar.setDisplayShowCustomEnabled(false);
            mActionBar.setDisplayShowHomeEnabled(true);
        }
    }
}
