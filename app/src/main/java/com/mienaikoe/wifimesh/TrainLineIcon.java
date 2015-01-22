package com.mienaikoe.wifimesh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class TrainLineIcon extends TypefaceTextView {

    private static Map<String, Integer> BACKGROUND_COLORS = new HashMap<String, Integer>();
    static{
        BACKGROUND_COLORS.put("A",R.color.train_ace_blue);
        BACKGROUND_COLORS.put("C",R.color.train_ace_blue);
        BACKGROUND_COLORS.put("E",R.color.train_ace_blue);

        BACKGROUND_COLORS.put("B",R.color.train_bdfm_orange);
        BACKGROUND_COLORS.put("D",R.color.train_bdfm_orange);
        BACKGROUND_COLORS.put("F",R.color.train_bdfm_orange);
        BACKGROUND_COLORS.put("M",R.color.train_bdfm_orange);

        BACKGROUND_COLORS.put("7",R.color.train_7_purple);

        BACKGROUND_COLORS.put("G",R.color.train_g_green);

        BACKGROUND_COLORS.put("1",R.color.train_123_red);
        BACKGROUND_COLORS.put("2",R.color.train_123_red);
        BACKGROUND_COLORS.put("3",R.color.train_123_red);

        BACKGROUND_COLORS.put("4",R.color.train_456_green);
        BACKGROUND_COLORS.put("5",R.color.train_456_green);
        BACKGROUND_COLORS.put("6",R.color.train_456_green);

        BACKGROUND_COLORS.put("J",R.color.train_jz_brown);
        BACKGROUND_COLORS.put("Z",R.color.train_jz_brown);

        BACKGROUND_COLORS.put("N",R.color.train_nqr_yellow);
        BACKGROUND_COLORS.put("Q",R.color.train_nqr_yellow);
        BACKGROUND_COLORS.put("R",R.color.train_nqr_yellow);

        BACKGROUND_COLORS.put("L",R.color.train_sl_gray);
        BACKGROUND_COLORS.put("GS",R.color.train_sl_gray);
        BACKGROUND_COLORS.put("FS",R.color.train_sl_gray);

        BACKGROUND_COLORS.put("SI",R.color.train_si_blue);
    }

    private static Map<String, Integer> FOREGROUND_COLORS = new HashMap<String, Integer>();
    static{
        FOREGROUND_COLORS.put("A",R.color.white);
        FOREGROUND_COLORS.put("C",R.color.white);
        FOREGROUND_COLORS.put("E",R.color.white);

        FOREGROUND_COLORS.put("B",R.color.white);
        FOREGROUND_COLORS.put("D",R.color.white);
        FOREGROUND_COLORS.put("F",R.color.white);
        FOREGROUND_COLORS.put("M",R.color.white);

        FOREGROUND_COLORS.put("7",R.color.white);

        FOREGROUND_COLORS.put("G",R.color.black);

        FOREGROUND_COLORS.put("1",R.color.white);
        FOREGROUND_COLORS.put("2",R.color.white);
        FOREGROUND_COLORS.put("3",R.color.white);

        FOREGROUND_COLORS.put("4",R.color.white);
        FOREGROUND_COLORS.put("5",R.color.white);
        FOREGROUND_COLORS.put("6",R.color.white);

        FOREGROUND_COLORS.put("J",R.color.white);
        FOREGROUND_COLORS.put("Z",R.color.white);

        FOREGROUND_COLORS.put("N",R.color.black);
        FOREGROUND_COLORS.put("Q",R.color.black);
        FOREGROUND_COLORS.put("R",R.color.black);

        FOREGROUND_COLORS.put("L",R.color.white);
        FOREGROUND_COLORS.put("GS",R.color.white);
        FOREGROUND_COLORS.put("FS",R.color.white);

        FOREGROUND_COLORS.put("SI",R.color.white);
    }


    private String symbol;
    private int size;



    public TrainLineIcon(Context context) {
        super(context);
        init(null, 0);
    }

    public TrainLineIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TrainLineIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        this.setCustomFont(this.getContext(), "fonts/HelveticaNeue-Medium.otf");

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TrainLineIcon, defStyle, 0);

        symbol = a.getString(R.styleable.TrainLineIcon_symbol);
        this.setText(symbol);

        size = a.getDimensionPixelSize(R.styleable.TrainLineIcon_size, 42);
        this.setHeight(size);
        this.setWidth(size);


        this.setGravity(Gravity.CENTER);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setTextSize(this.size * 0.25F); // What are the parameter units?? Causing weird sizing on different devices

        this.setTextColor(this.getForegroundColor());
        this.setBackgroundColor(getResources().getColor(R.color.transparent));

        a.recycle();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        Paint painter = new Paint();
        painter.setColor(this.getBackgroundColor());
        canvas.drawCircle(this.size/2, this.size/2, this.size/2, painter);

        super.onDraw(canvas);
    }




    private int getForegroundColor(){
        return getResources().getColor(FOREGROUND_COLORS.get(this.symbol));
    }

    private int getBackgroundColor(){
        return getResources().getColor(BACKGROUND_COLORS.get(this.symbol));
    }
}
