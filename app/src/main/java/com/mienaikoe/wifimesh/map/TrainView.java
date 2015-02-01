package com.mienaikoe.wifimesh.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;

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

    private float mScaleFactor = 0.6f;
    private float scalePointX = 0.f;
    private float scalePointY = 0.f;
    private ScaleGestureDetector mScaleDetector;

    private float startX = 0;
    private float startY = 0;
    private float deltaX = 200;
    private float deltaY = -50;
    private float maxX;
    private float maxY;


    private List<VectorInstruction> lines = new ArrayList<VectorInstruction>(0);
    private List<VectorInstruction> linesText = new ArrayList<VectorInstruction>(0);
    private List<VectorInstruction> stationsText = new ArrayList<VectorInstruction>(0);
    private List<VectorInstruction> crossStreets = new ArrayList<VectorInstruction>(0);




    public TrainView(Context context) {
        super(context);
        init(context);
    }

    public TrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TrainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.maxX = size.x;
        this.maxY = size.y;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.setBackgroundColor(this.getResources().getColor(R.color.light_gray));

        canvas.translate(deltaX / mScaleFactor, deltaY / mScaleFactor);

        Log.i("SCALE FACTOR", mScaleFactor+"");

        canvas.scale(
                mScaleFactor, mScaleFactor,
                scalePointX - deltaX, scalePointY - deltaY);

        for( VectorInstruction crossStreet : this.crossStreets ){
            crossStreet.draw(canvas);
        }

        for( VectorInstruction line : this.lines ){
            line.draw(canvas);
        }
        for( VectorInstruction lineText : this.linesText ){
            lineText.draw(canvas);
        }
        for( VectorInstruction stationText : this.stationsText ){
            stationText.draw(canvas);
        }

        Paint stationPainter = new Paint();
        stationPainter.setColor(getResources().getColor(R.color.white));
        for(TrainStation station : this.system.getStations()){
            for( RectF rect : station.getMapRectangles() ){
                canvas.drawRect( rect, stationPainter );
            }
        }

    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 5.0f));

            scalePointX = detector.getFocusX();
            scalePointY = detector.getFocusY();

            return true;
        }
    }





    private int mActivePointerId = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        mScaleDetector.onTouchEvent(ev);

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                startX = MotionEventCompat.getX(ev, pointerIndex) - deltaX;
                startY = MotionEventCompat.getY(ev, pointerIndex) - deltaY;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if( MotionEventCompat.getPointerCount(ev) <= 1 ) {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    if (pointerIndex == -1) {
                        mActivePointerId = -1;
                        break;
                    }
                    float atX = MotionEventCompat.getX(ev, pointerIndex);
                    float atY = MotionEventCompat.getY(ev, pointerIndex);
                    deltaX = atX - startX;
                    deltaY = atY - startY;
                    break;
                }
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
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    startX = MotionEventCompat.getX(ev, newPointerIndex) - deltaX;
                    startY = MotionEventCompat.getY(ev, newPointerIndex) - deltaY;
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }

        this.invalidate();
        return true;
    }


    public void setCenter( float x, float y ){
        mScaleFactor = 2.5f;
        this.deltaX = -(x * mScaleFactor) + (getResources().getDisplayMetrics().widthPixels/2);
        this.deltaY = -(y * mScaleFactor) + (getResources().getDisplayMetrics().heightPixels/2);
        invalidate();
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

    public void setLinesText(List<VectorInstruction> paths){
        this.linesText = paths;
    }

    public void setStationsText(List<VectorInstruction> paths){
        this.stationsText = paths;
    }
}
