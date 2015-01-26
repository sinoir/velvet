package com.mienaikoe.wifimesh.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

/**
 * TODO: document your custom view class.
 */
public class TrainView extends View {

    private TrainSystem system;
    private TrainStation currentStation;

    private float mScaleFactor = 1.f;
    private float scalePointX = 0.f;
    private float scalePointY = 0.f;
    private ScaleGestureDetector mScaleDetector;




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

        canvas.save();

        canvas.scale(mScaleFactor, mScaleFactor, scalePointX, scalePointY);

        canvas.translate(dx / mScaleFactor, dy / mScaleFactor);

        float trainWidth = (float) 5.0f * (1.0f / mScaleFactor);

        for( TrainLine line : this.system.getLines() ){
            Paint linePainter = new Paint();
            linePainter.setColor(getResources().getColor(line.getBackgroundColor()));
            linePainter.setStrokeWidth(trainWidth);

            TrainStation lastStop = null;
            for( TrainStation stop : line.getSouthStops() ){
                if( lastStop != null ){
                    canvas.drawLine(
                            lastStop.getViewX(), lastStop.getViewY(),
                            stop.getViewX(), stop.getViewY(),
                            linePainter );
                }
                lastStop = stop;
            }
        }


        Paint stationPainter = new Paint();
        stationPainter.setColor(getResources().getColor(R.color.black));
        for(TrainStation station : this.system.getStations()){
            canvas.drawCircle( station.getViewX(), station.getViewY(), trainWidth, stationPainter );
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



    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId;
    private float startX;
    private float startY;
    private float dx;
    private float dy;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = MotionEventCompat.getActionMasked(ev);

            switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = 0;//MotionEventCompat.getActionIndex(ev);
                startX = MotionEventCompat.getX(ev, pointerIndex);
                startY = MotionEventCompat.getY(ev, pointerIndex);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if( pointerIndex == -1 ){
                    return true;
                }
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                dx = x - startX;
                dy = y - startY;
                invalidate();
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = -1;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    mActivePointerId = -1;
                    break;
                }
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
}
