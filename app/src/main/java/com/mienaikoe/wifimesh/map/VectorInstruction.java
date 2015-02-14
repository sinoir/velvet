package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;

/**
 * Created by Jesse on 1/29/2015.
 */
public interface VectorInstruction {

    public void draw(Canvas canvas, Matrix transformationMatrix, Matrix inverseTransformation, float scalingFactor);

}
