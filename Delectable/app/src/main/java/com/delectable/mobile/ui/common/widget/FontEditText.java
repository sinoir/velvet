package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.FontEnum;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class FontEditText extends EditText {

    public FontEditText(Context context) {
        super(context);
        init(null);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FontTextView);
            int fontIntValue = a.getInt(R.styleable.FontTextView_fontName,
                    FontEnum.WHITNEY_BLACK.ordinal());
            a.recycle();

            FontEnum font = FontEnum.values()[fontIntValue];
            setTypeface(font);
        }
    }

    /**
     * Convenience method that calls {@link #setTypeface(android.graphics.Typeface)}
     */
    public void setTypeface(FontEnum font) {
        Typeface typeface = Typeface
                .createFromAsset(getContext().getAssets(), "fonts/" + font.getFileName());
        setTypeface(typeface);
    }
}
