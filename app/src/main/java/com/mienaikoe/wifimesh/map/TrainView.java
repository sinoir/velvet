package com.mienaikoe.wifimesh.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.mienaikoe.wifimesh.R;

/**
 * TODO: document your custom view class.
 */
public class TrainView extends View {

    private Drawable map;

    public TrainView(Context context) {
        super(context);
    }

    public TrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if( map != null ) {
            map.draw(canvas);
        }
    }

    public void setMap(Drawable map) {
        this.map = map;
    }
}
