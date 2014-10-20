package com.delectable.mobile.ui.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.delectable.mobile.R;

/**
 * Delectabutton. The button that comes in different styles and with cool features.
 */
public class Delectabutton extends RelativeLayout {

    public static enum Type {
        LARGE;
    }

    private FontTextView mTextView;

    private ImageView mIcon;

    private Type mType = Type.LARGE;

    private String mText = "";

    private Drawable mIconDrawable;

    public Delectabutton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Delectabutton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Delectabutton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Delectabutton, defStyle, 0);

        mType = Type.values()[a.getInt(R.styleable.Delectabutton_type, 0)];


        if (Type.LARGE == mType) {
            View.inflate(context, R.layout.widget_delectabutton_large, this);
        } else {
            // default
            View.inflate(context, R.layout.widget_delectabutton_large, this);
        }

        mIcon = (ImageView) findViewById(R.id.delectabutton_icon);

        mTextView = (FontTextView) findViewById(R.id.delectabutton_text);
        mTextView.setTextColor(a.getColor(R.styleable.Delectabutton_android_textColor,
                getResources().getColor(R.color.d_dark_gray)));

        setIconDrawable(a.getDrawable(
                R.styleable.Delectabutton_icon));

        setText(a.getString(
                R.styleable.Delectabutton_android_text));

        a.recycle();
    }

    /**
     * Gets the icon drawable attribute value.
     *
     * @return The icon drawable attribute value.
     */
    public Drawable getIconDrawable() {
        return mIconDrawable;
    }

    /**
     * Sets the buttons icon drawable attribute value.
     *
     * @param iconDrawable The icon drawable attribute value to use.
     */
    public void setIconDrawable(Drawable iconDrawable) {
        mIconDrawable = iconDrawable;
        mIcon.setBackgroundDrawable(iconDrawable);
        mIcon.setVisibility(mIconDrawable != null ? View.VISIBLE : View.GONE);
    }

    public String getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        mText = text.toString();
        mTextView.setText(text);
        if (mIconDrawable == null) {
            // remove left pedding for text when there is no icon
            mTextView.setPadding(0, mTextView.getPaddingTop(), mTextView.getPaddingRight(),
                    mTextView.getPaddingBottom());
        }
    }

    public void setText(int resourceId) {
        mText = getResources().getString(resourceId);
        mTextView.setText(mText);
    }

}
