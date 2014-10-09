package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.FontEnum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Delectabutton. The button that comes in different styles and with cool features.
 */
public class Delectabutton extends FrameLayout {

    private ViewGroup mLayout;

    private TextView mTextView;

    private ImageView mIcon;

    private String mType = "large";

    private String mText = "";

    private Drawable mIconDrawable;

    private boolean mEnabled = true;

    public Delectabutton(Context context) {
        super(context);
        init(null, 0);
    }

    public Delectabutton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Delectabutton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Delectabutton, defStyle, 0);

        if (a.hasValue(R.styleable.Delectabutton_type)) {
            mType = a.getString(
                    R.styleable.Delectabutton_type);
        }

        View v;
        FontEnum font;

        if ("large".equals(mType)) {
            v = inflater.inflate(R.layout.widget_delectabutton_large, null, false);
            font = FontEnum.WHITNEY_BOOK_SC;
        } else {
            // default
            v = inflater.inflate(R.layout.widget_delectabutton_large, null, false);
            font = FontEnum.WHITNEY_BOOK_SC;
        }

        mLayout = (ViewGroup) v.findViewById(R.id.delectabutton_layout);
        mLayout.setClickable(true);

        mIcon = (ImageView) v.findViewById(R.id.delectabutton_icon);

        mTextView = (TextView) v.findViewById(R.id.delectabutton_text);
        mTextView.setTypeface(Typeface
                .createFromAsset(getContext().getAssets(), "fonts/" + font.getFileName()));

        if (a.hasValue(R.styleable.Delectabutton_icon)) {
            setIconDrawable(a.getDrawable(
                    R.styleable.Delectabutton_icon));
        }

        if (a.hasValue(R.styleable.Delectabutton_android_text)) {
            setText(a.getString(
                    R.styleable.Delectabutton_android_text));
        }

        if (a.hasValue(R.styleable.Delectabutton_android_enabled)) {
            setEnabled(a.getBoolean(R.styleable.Delectabutton_android_enabled, true));
        }

        a.recycle();

        addView(v);

    }

    public void setOnClickListener(OnClickListener listener) {
        mLayout.setOnClickListener(listener);
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

    public boolean getEnabled() {
        return mEnabled;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        mLayout.setEnabled(enabled);
    }

}
