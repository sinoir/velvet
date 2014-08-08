package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView {

    //This is mapped directly to attrs.xml's fontName enum, changes here should be reflected there as well
    public enum Font {
        WHITNEY_BLACK("Whitney-Black-Adv.otf"),
        WHITNEY_BLACK_ITAL("Whitney-BlackItal-Adv.otf"),
        WHITNEY_BOLD("Whitney-Bold-Adv.otf"),
        WHITNEY_BOLD_ITAL("Whitney-BoldItal-Adv.otf"),
        WHITNEY_BOLD_SC("Whitney-BoldSC.otf"),
        WHITNEY_BOOK("Whitney-Book-Adv.otf"),
        WHITNEY_BOOK_ITAL("Whitney-BookItal-Adv.otf"),
        WHITNEY_BOOK_SC("Whitney-BookSC.otf"),
        WHITNEY_LIGHT("Whitney-Light-Adv.otf"),
        WHITNEY_LIGHT_ITAL("Whitney-LightItal-Adv.otf"),
        WHITNEY_MEDIUM("Whitney-Medium-Adv.otf"),
        WHITNEY_MEDIUM_ITAL("Whitney-MediumItal-Adv.otf"),
        WHITNEY_MEDIUM_SC("Whitney-MediumSC.otf"),
        WHITNEY_SEMIBOLD("Whitney-Semibold-Adv.otf"),
        WHITNEY_SEMIBOLD_ITAL("Whitney-SemiboldItal-Adv.otf"),
        WHITNEY_SEMIBOLD_SC("Whitney-SemiboldSC.otf");

        private String mFileName;

        private Font(String fileName) {
            mFileName = fileName;
        }

        public String getFileName() {
            return mFileName;
        }
    }

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        int fontIntValue = a
                .getInt(R.styleable.FontTextView_fontName, Font.WHITNEY_BLACK.ordinal());
        a.recycle();

        Font font = Font.values()[fontIntValue];
        setTypeface(font);
    }

    /**
     * Convenience method that calls {@link #setTypeface(android.graphics.Typeface)}
     */
    public void setTypeface(Font font) {
        Typeface typeface = Typeface
                .createFromAsset(getContext().getAssets(), "fonts/" + font.getFileName());
        setTypeface(typeface);
    }


}
