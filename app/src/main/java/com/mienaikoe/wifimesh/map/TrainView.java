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
import com.mienaikoe.wifimesh.StartupActivity;
import com.mienaikoe.wifimesh.StationSelectEvent;
import com.mienaikoe.wifimesh.TrainMapFragment;
import com.mienaikoe.wifimesh.train.TrainLine;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import de.greenrobot.event.EventBus;

/**
 * TODO: document your custom view class.
 */
public class TrainView extends View {

    private TrainSystem system;
    private TrainStation currentStation;

    private float mScaleFactor = 1.0f;
    private float scalePointX = 0.f;
    private float scalePointY = 0.f;
    private ScaleGestureDetector mScaleDetector;

    private float startX = 0;
    private float startY = 0;
    private float deltaX = 200;
    private float deltaY = -50;


    private VectorMapIngestor ingestor;

    private ArrayList<VectorInstruction> crossStreetInstructions = new ArrayList<VectorInstruction>();
    private ArrayList<VectorInstruction> mapInstructions = new ArrayList<VectorInstruction>();


    private EventBus mEventBus = EventBus.getDefault();


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
        this.setBackgroundColor(Color.parseColor("#E3E2D5"));
    }


    private String[] STANDARD_GROUPS = new String[]{
            "PARKS", "PARKS_TEXT", "NEIGHBORHOOD_NAMES", "TRAIN_LINES", "TRANSFERS", "STATION_BLOCKS", "TRAIN_NAMES", "STATION_NAMES",
    };

    private String[] CROSS_STREET_GROUPS = new String[]{
            "CROSS_STREETS","STREET_NAMES"
    };


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.scale( mScaleFactor, mScaleFactor, scalePointX - deltaX, scalePointY - deltaY);

        canvas.translate(deltaX / mScaleFactor, deltaY / mScaleFactor);

        for( VectorInstruction instruction : this.crossStreetInstructions ){
            instruction.draw(canvas);
        }

        if( this.currentStation != null ) {
            Paint arcPaint = new Paint();
            arcPaint.setStrokeWidth(8.0f);
            arcPaint.setColor(Color.parseColor("#ffffff"));
            arcPaint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(new RectF(
                    this.currentStation.getViewX() - 24,
                    this.currentStation.getViewY() - 24,
                    this.currentStation.getViewX() + 24,
                    this.currentStation.getViewY() + 24
            ), 0, 360, false, arcPaint);
        }

        for( VectorInstruction instruction : this.mapInstructions ){
            instruction.draw(canvas);
        }
        //canvas.drawCircle(this.clickedX, this.clickedY, 15, new Paint());
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
    private Long lastTouch = null;
    private boolean moved = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        mScaleDetector.onTouchEvent(ev);

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                lastTouch = new Date().getTime();
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                startX = MotionEventCompat.getX(ev, pointerIndex) - deltaX;
                startY = MotionEventCompat.getY(ev, pointerIndex) - deltaY;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                moved = true;
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
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                /*
                if( !moved && new Date().getTime() - lastTouch < 500 ){
                    final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                    onClick(
                            -deltaX + (MotionEventCompat.getX(ev, pointerIndex)),
                            -deltaY + (MotionEventCompat.getY(ev, pointerIndex))
                    );
                }
                */
                moved = false;
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



    private static final int CLICK_THRESHOLD_DISTANCE = 20;

    private float clickedX;
    private float clickedY;

    private void onClick( float x, float y ){
        Log.i(this.getClass().getSimpleName(), "On Clicking: ["+x+","+y+"]");
        clickedX = x;
        clickedY = y;
        for( TrainStation station : this.system.getStations() ){
            if( Math.abs(station.getViewX() - x) < CLICK_THRESHOLD_DISTANCE &&
                Math.abs(station.getViewY() - y) < CLICK_THRESHOLD_DISTANCE ){

                Log.i(this.getClass().getSimpleName(), "MATCHED!!! "+Math.abs(station.getViewX() - x)+":"+Math.abs(station.getViewY() - y));
                Log.i(this.getClass().getSimpleName(), "MATCHED!!! "+station.getName());

                mEventBus.postSticky(new StationSelectEvent(station));
                return;
            }
        }
    }




    public void setSystem(TrainSystem system) {
        this.system = system;
    }

    public void setIngestor(VectorMapIngestor ingestor){
        this.ingestor = ingestor;
        for( String groupName : CROSS_STREET_GROUPS ) {
            this.crossStreetInstructions.addAll(this.ingestor.getInstructionGroup(groupName));
        }
        for( String groupName : STANDARD_GROUPS ) {
           this.mapInstructions.addAll(this.ingestor.getInstructionGroup(groupName));
        }
    }

    public void setStation(TrainStation currentStation) {
        this.currentStation = currentStation;
        this.setCenter(currentStation.getViewX(), currentStation.getViewY());
    }

    private void setCenter( float x, float y ){
        mScaleFactor = 2.5f;
        this.scalePointX = 0.f;
        this.scalePointY = 0.f;
        this.deltaX = -x + (this.getWidth()/(2*mScaleFactor));
        this.deltaY = -y + (this.getHeight()/(2*mScaleFactor));
        invalidate();
    }

}
