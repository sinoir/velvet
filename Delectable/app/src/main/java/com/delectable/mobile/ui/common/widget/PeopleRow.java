package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.ImageLoaderUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PeopleRow extends RelativeLayout {

    @InjectView(R.id.row_image)
    CircleImageView mImage;

    @InjectView(R.id.row_name)
    TextView mName;

    @InjectView(R.id.row_rating)
    RatingTextView mRating;

    public PeopleRow(Context context) {
        this(context, null);
    }

    public PeopleRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PeopleRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.row_people, this);
        ButterKnife.inject(this);
    }

    public void updateData(String profileImageUrl, String name) {
        updateData(profileImageUrl, name, -1);
    }

    public void updateData(String profileImageUrl, String name, int rating) {

        //set no_photo first, so that when the user flicks through the list, it doesn't show another account's picture
        ImageLoaderUtil.loadImageIntoView(getContext(), R.drawable.no_photo, mImage);
        if (profileImageUrl != null) {
            ImageLoaderUtil.loadImageIntoView(getContext(), profileImageUrl, mImage);
        }

        mName.setText(name);

        if (rating > -1) {
            mRating.setRatingOf40(rating);
            mRating.setVisibility(View.VISIBLE);
        } else {
            mRating.setVisibility(View.GONE);
        }
    }
}
