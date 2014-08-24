package com.delectable.mobile.ui.tagpeople.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TagPeopleRow extends RelativeLayout {

    @InjectView(R.id.user_name)
    TextView mUserName;

    public TagPeopleRow(Context context) {
        this(context, null);
    }

    public TagPeopleRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagPeopleRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_tag_people, this);
        ButterKnife.inject(this);
    }

    public void updateData(String name) {
        mUserName.setText(name);
    }
}
