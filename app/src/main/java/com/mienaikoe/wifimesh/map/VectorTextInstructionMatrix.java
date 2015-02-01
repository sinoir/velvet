package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;

/**
 * Created by Jesse on 2/1/2015.
 */
public class VectorTextInstructionMatrix extends VectorTextInstruction {

    private Matrix matrix;

    public VectorTextInstructionMatrix(VectorTextInstruction copy) {
        super(copy);
        if( copy instanceof VectorTextInstructionMatrix ){
            this.matrix = ((VectorTextInstructionMatrix) copy).matrix;
        }
    }

    public VectorTextInstructionMatrix(String text, String matrix, int color, Typeface typeface) {
        super(text, matrix, color, typeface);
        this.matrix = PathParser.parseTransform(matrix);
    }


    public void draw( Canvas canvas ){
        Matrix original = canvas.getMatrix();
        Matrix concat = new Matrix();
        concat.setConcat(original, this.matrix);
        canvas.setMatrix(concat);
        super.draw(canvas);
        canvas.setMatrix(original);
    }
}
