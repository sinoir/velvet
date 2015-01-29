package com.mienaikoe.wifimesh;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class TypefaceTextView extends TextView {

    private static Map<String, Typeface> typefaces = new HashMap<String, Typeface>();



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
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView);
        String customFont = a.getString(R.styleable.TypefaceTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String customFont){
        Typeface tf = getTypeface(ctx, customFont);
        setTypeface(tf);
    }

    public static Typeface getTypeface(Context ctx, String asset) {
        Typeface typeface;
        if( typefaces.containsKey(asset) ) {
            typeface = typefaces.get(asset);
        } else {
            try {
                typeface = Typeface.createFromAsset(ctx.getAssets(), asset);
                typefaces.put(asset, typeface);
            } catch (RuntimeException ex) {
                typeface = Typeface.DEFAULT;
            }
        }
        return typeface;
    }

}
