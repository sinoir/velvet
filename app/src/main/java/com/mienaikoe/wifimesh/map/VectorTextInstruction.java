package com.mienaikoe.wifimesh.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.mienaikoe.wifimesh.TypefaceTextView;

/**
 * Created by Jesse on 1/29/2015.
 */
public class VectorTextInstruction implements VectorInstruction {

    private Paint paint;
    private String text = null;
    private float x;
    private float y;

    public VectorTextInstruction(String text, String matrix, int color, Typeface typeface, float fontSize){
        if( text == null ){
            text = "";
        }
        this.text = text;

        matrix = matrix.substring(7, matrix.length()-1);
        String[] matrixArr = matrix.split(" ");
        this.x = Float.valueOf(matrixArr[matrixArr.length-2]);
        this.y = Float.valueOf(matrixArr[matrixArr.length-1]);

        this.paint = new Paint();
        this.paint.setColor(color);
        this.paint.setTypeface(typeface);
        this.paint.setTextSize(fontSize);
    }

    public VectorTextInstruction(VectorTextInstruction copy){
        this.text = copy.text;
        this.paint = copy.paint;
        this.x = copy.x;
        this.y = copy.y;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color){
        this.paint.setColor(color);
    }

    public void addOffset(float x, float y){
        this.x += x;
        this.y += y;
    }

    public boolean hasText(){
        return this.text != null;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(this.text, this.x, this.y, this.paint);
    }

    public void setFontSize(float fontSize) {
        this.paint.setTextSize(fontSize);
    }
}
