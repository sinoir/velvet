package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Jesse on 2/14/2015.
 */
public class VectorPolygonInstruction implements VectorInstruction {

    private Path path;
    private Paint paint;


    public VectorPolygonInstruction( float[] points, int fillColor ){
        this.path = new Path();
        this.path.moveTo(points[0],points[1]);
        for( int i=2; i < points.length; i+=2 ){
            this.path.lineTo(points[i], points[i+1]);
        }
        this.path.lineTo(points[0],points[1]);

        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(fillColor);
    }


    @Override
    public void draw(Canvas canvas, Matrix transformationMatrix, Matrix inverserTransformation, float scalingFactor) {
        this.path.transform(transformationMatrix);
        canvas.drawPath( this.path, this.paint );
        this.path.transform(inverserTransformation);
    }
}
