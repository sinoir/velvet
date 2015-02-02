package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Jesse on 2/1/2015.
 */
public class VectorRectangleInstruction implements VectorInstruction {

    private float x;
    private float y;
    private float width;
    private float height;
    private Paint paint;

    public VectorRectangleInstruction( float x, float y, float width, float height, int color ){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.paint = new Paint();
        this.paint.setColor(color);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect( x, y, x+width, y+height, paint );
    }


    public float getCenterX(){
        return this.x + (this.width / 2);
    }

    public float getCenterY(){
        return this.y + (this.height / 2);
    }
}
