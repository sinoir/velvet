package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Jesse on 1/29/2015.
 */
public class VectorLineInstruction implements VectorInstruction {

    private float x1;
    private float y1;

    private float x2;
    private float y2;

    private Paint paint;


    public VectorLineInstruction( float x1, float y1, float x2, float y2, int color, float width ){
        this.x1 = x1;
        this.y1 = y1;

        this.x2 = x2;
        this.y2 = y2;

        this.paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(width);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(x1, y1, x2, y2, paint);
    }
}
