package com.mienaikoe.wifimesh.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.mienaikoe.wifimesh.R;
import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class TrainView extends View {

    private TrainSystem system;
    private TrainStation currentStation;

    private float mScaleFactor = 0.7f;
    private float scalePointX = 0.f;
    private float scalePointY = 0.f;
    private ScaleGestureDetector mScaleDetector;

    private int mActivePointerId = -1;
    private float startX = 0;
    private float startY = 0;
    private float deltaX = 0;
    private float deltaY = 0;


    private List<VectorInstruction> lines = new ArrayList<VectorInstruction>(0);
    private List<VectorInstruction> crossStreets = new ArrayList<VectorInstruction>(0);




    public TrainView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public TrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public TrainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.setBackgroundColor(this.getResources().getColor(R.color.light_gray));

        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor, scalePointX, scalePointY);
        canvas.translate(deltaX / mScaleFactor, deltaY / mScaleFactor);

        for( VectorInstruction crossStreet : this.crossStreets ){
            crossStreet.draw(canvas);
        }

        for( VectorInstruction line : this.lines ){
            line.draw(canvas);
        }

        Paint stationPainter = new Paint();
        stationPainter.setColor(getResources().getColor(R.color.white));
        for(TrainStation station : this.system.getStations()){
            for( Rect rect : station.getMapRectangles() ){
                canvas.drawRect( rect, stationPainter );
            }
        }

        canvas.restore();
    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            scalePointX = detector.getFocusX();
            scalePointY = detector.getFocusY();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }




    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);


        final int action = MotionEventCompat.getActionMasked(ev);
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        if( pointerIndex != 0 ){
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                startX = MotionEventCompat.getX(ev, pointerIndex) - deltaX;
                startY = MotionEventCompat.getY(ev, pointerIndex) - deltaY;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                float atX = MotionEventCompat.getX(ev, pointerIndex);
                float atY = MotionEventCompat.getY(ev, pointerIndex);
                deltaX = atX - startX;
                deltaY = atY - startY;
                invalidate();
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = -1;
                break;
            }
        }
        return true;
    }


    public void setCenter( float x, float y ){
        //mScaleFactor = 1.0f;
        //dx = x;
        //dy = y;
        // This isn't working right
    }

    public void setSystem(TrainSystem system) {
        this.system = system;
    }

    public void setStation(TrainStation currentStation) {
        this.currentStation = currentStation;
    }

    public void setLines(List<VectorInstruction> paths){
        this.lines = paths;
    }
}
