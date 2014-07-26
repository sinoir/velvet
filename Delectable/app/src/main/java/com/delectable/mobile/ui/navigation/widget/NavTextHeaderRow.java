package com.delectable.mobile.ui.navigation.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavTextHeaderRow extends RelativeLayout {

    private TextView mTitle;

    public NavTextHeaderRow(Context context) {
        this(context, null);
    }

    public NavTextHeaderRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavTextHeaderRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.row_nav_text_header, this);

        mTitle = (TextView) findViewById(R.id.header_title);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }
}
