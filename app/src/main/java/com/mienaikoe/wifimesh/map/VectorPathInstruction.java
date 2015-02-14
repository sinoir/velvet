package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Jesse on 1/29/2015.
 */
public class VectorPathInstruction implements VectorInstruction {

    private Path path;
    private Paint paint;
    private float width;

    public VectorPathInstruction(String pathString, int color, float width){
        this.path = PathParser.parsePath( pathString );

        this.paint = new Paint();
        paint.setColor(color);
        this.width = width;
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas, Matrix transformationMatrix, Matrix inverserTransformation, float scalingFactor) {
        this.path.transform(transformationMatrix);
        paint.setStrokeWidth(width * scalingFactor );
        canvas.drawPath(this.path, this.paint);
        this.path.transform(inverserTransformation);
    }
}
