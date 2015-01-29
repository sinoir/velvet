package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Jesse on 1/29/2015.
 */
public class VectorPathInstruction implements VectorInstruction {

    private Path path;
    private Paint paint;

    public VectorPathInstruction(String pathString, int color, float width){
        this.path = PathParser.parsePath( pathString );

        this.paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(width);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(this.path, this.paint);
    }
}
