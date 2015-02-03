package com.mienaikoe.wifimesh;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

public class TypefaceTextView extends TextView {

    public TypefaceTextView(Context context) {
        super(context);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);
        int fontIntValue = a.getInt(R.styleable.TypefaceTextView_customFont,
                FontEnum.HELVETICA_NEUE.ordinal());
        a.recycle();

        FontEnum font = FontEnum.values()[fontIntValue];
        setTypeface(font);
    }

    public void setTypeface(FontEnum font) {
        setTypeface(font.getTypeface(getContext()));
    }
}
