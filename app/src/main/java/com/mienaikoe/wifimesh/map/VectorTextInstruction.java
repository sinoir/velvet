package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.mienaikoe.wifimesh.TypefaceTextView;

/**
 * Created by Jesse on 1/29/2015.
 */
public class VectorTextInstruction implements VectorInstruction {

    private Paint paint;
    private String text = null;
    private Matrix matrix;

    public VectorTextInstruction(String text, String matrixStr, int color, Typeface typeface, float fontSize){
        if( text == null ){
            text = "";
        }
        this.text = text;

        if( matrixStr != null ) {
            this.matrix = PathParser.parseTransform(matrixStr);
        } else {
            this.matrix = new Matrix();
        }

        this.paint = new Paint();
        this.paint.setColor(color);
        this.paint.setTypeface(typeface);
        this.paint.setTextSize(fontSize);
    }

    public VectorTextInstruction(VectorTextInstruction copy){
        this.text = copy.text;
        this.paint = copy.paint;
        this.matrix = new Matrix(copy.matrix);
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color){
        this.paint.setColor(color);
    }

    public void addOffset(float x, float y){
        this.matrix.postTranslate(x, y);
    }

    public boolean hasText(){
        return this.text != null;
    }

    @Override
    public void draw(Canvas canvas) {
        Matrix inverse = new Matrix();
        this.matrix.invert(inverse);
        canvas.concat(this.matrix);
        canvas.drawText(this.text, 0, 0, this.paint);
        canvas.concat(inverse);
    }

    public void setFontSize(float fontSize) {
        this.paint.setTextSize(fontSize);
    }
}
