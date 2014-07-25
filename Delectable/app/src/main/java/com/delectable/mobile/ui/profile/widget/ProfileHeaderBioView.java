package com.delectable.mobile.ui.profile.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfileHeaderBioView extends RelativeLayout {

    private TextView mUserBioTextView;

    public ProfileHeaderBioView(Context context) {
        this(context, null);
    }

    public ProfileHeaderBioView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileHeaderBioView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        View.inflate(context, R.layout.profile_header_bio, this);

        mUserBioTextView = (TextView) findViewById(R.id.user_bio_text);
    }

    public void setUserBio(String userBio) {
        mUserBioTextView.setText(userBio);
    }
}
