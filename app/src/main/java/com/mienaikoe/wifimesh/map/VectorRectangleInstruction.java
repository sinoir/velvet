package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by Jesse on 2/1/2015.
 */
public class VectorRectangleInstruction implements VectorInstruction {

    private Matrix matrix;
    private Matrix inverse;
    private float width;
    private float height;
    private Paint paint;

    public VectorRectangleInstruction( float x, float y, String matrixStr, float width, float height, int color ){
        if( matrixStr != null ) {
            this.matrix = PathParser.parseTransform(matrixStr);
        } else {
            this.matrix = new Matrix();
        }
        this.matrix.preTranslate(x, y);
        this.inverse = new Matrix();
        this.matrix.invert(this.inverse);
        this.width = width;
        this.height = height;
        this.paint = new Paint();
        this.paint.setColor(color);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.concat(this.matrix);
        canvas.drawRect( 0, 0, width, height, paint );
        canvas.restore();
    }


    public float[] getCenter(){
        float[] ret = new float[]{ 0, 0 };
        this.matrix.mapPoints(ret);
        ret[0] += this.width/2;
        ret[1] += this.height/2;
        return ret;
    }

}
