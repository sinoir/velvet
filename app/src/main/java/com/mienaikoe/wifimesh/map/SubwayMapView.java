package com.mienaikoe.wifimesh.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.mienaikoe.wifimesh.StationSelectEvent;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * TODO: document your custom view class.
 */
public class SubwayMapView extends View {

    private TrainSystem system;
    private TrainStation currentStation;

    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector;

    private float startX = 0f;          // Screen Coordinates
    private float startY = 0f;          // Screen Coordinates

    private float currentX = 0f;        // Screen Coordinates
    private float currentY = 0f;        // Screen Coordinates

    private float translateX = 0f;      // Map Coordinates, 1:1 scale
    private float translateY = -300f;   // Map Coordinates, 1:1 scale

    private float scaleX = 0.f;         // Screen Coordinates
    private float scaleY = 0.f;         // Screen Coordinates


    private VectorMapIngestor ingestor;

    private ArrayList<VectorInstruction> crossStreetInstructions = new ArrayList<VectorInstruction>();
    private ArrayList<VectorInstruction> mapInstructions = new ArrayList<VectorInstruction>();


    private EventBus mEventBus = EventBus.getDefault();


    public SubwayMapView(Context context) {
        super(context);
        init(context);
    }

    public SubwayMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SubwayMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private static int WATER_COLOR = Color.parseColor("#82A4AD");
    private static int LAND_COLOR = Color.parseColor("#E3E2D5");

    private String[] STANDARD_GROUPS = new String[]{
            "TRAIN_LINES", "TRANSFERS", "STATION_BLOCKS", "TRAIN_NAMES", "STATION_NAMES",
    };

    private String[] CROSS_STREET_GROUPS = new String[]{
            "MANHATTAN_LAND","CROSS_STREETS","STREET_NAMES","NEIGHBORHOOD_NAMES", "PARKS", "PARKS_TEXT"
    };

    private static Paint LOCATION_PAINT = new Paint();
    static{
        LOCATION_PAINT.setStrokeWidth(8.0f);
        LOCATION_PAINT.setColor(Color.parseColor("#ffffff"));
        LOCATION_PAINT.setStyle(Paint.Style.STROKE);
    }

    private static Paint TEST_PAINT = new Paint();
    static{
        TEST_PAINT.setColor(Color.parseColor("#000000"));
        TEST_PAINT.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Yay Matrix Math
        Matrix transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(translateX, translateY);
        transformationMatrix.postScale(mScaleFactor, mScaleFactor, scaleX, scaleY);

        canvas.concat(transformationMatrix);

        if( this.showingStreets ) {
            for (VectorInstruction instruction : this.crossStreetInstructions) {
                instruction.draw(canvas);
            }
            this.setBackgroundColor(WATER_COLOR);
        } else {
            this.setBackgroundColor( LAND_COLOR );
        }

        if( this.currentStation != null ) {
            float[] stationVectorCenter = this.currentStation.getVectorCenter();
            canvas.drawArc(new RectF(
                    stationVectorCenter[0] - 24, stationVectorCenter[1] - 24,
                    stationVectorCenter[0] + 24, stationVectorCenter[1] + 24
            ), 0, 360, false, LOCATION_PAINT);
        }

        for( VectorInstruction instruction : this.mapInstructions ){
            instruction.draw(canvas);
        }

        canvas.drawCircle( (-translateX + scaleX), (-translateY + scaleY), 8.0f, TEST_PAINT );
    }


    private boolean showingStreets = true;

    public void toggleStreets() {
        showingStreets = !showingStreets;
        this.invalidate();
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 5.0f));

            scaleX = detector.getFocusX();
            scaleY = detector.getFocusY();

            return true;
        }
    }





    private int mActivePointerId = -1;
    private boolean isScaling = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if( ev.getPointerCount() > 1 ){
            mScaleDetector.onTouchEvent(ev);
        }

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                startX = (MotionEventCompat.getX(ev, 0)); // Screen Coordinates
                startY = (MotionEventCompat.getY(ev, 0)); // Screen Coordinates
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                if( !isScaling ) {
                    mActivePointerId = -1;
                    startX = (scaleX);
                    startY = (scaleY);
                    isScaling = true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float deltaX, deltaY;
                if( isScaling ){
                    deltaX = scaleX - startX;
                    deltaY = scaleY - startY;
                } else {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    if (pointerIndex == -1) {
                        mActivePointerId = -1;
                        break;
                    }
                    deltaX = MotionEventCompat.getX(ev, pointerIndex) - startX; // 1:1 scale delta
                    deltaY = MotionEventCompat.getY(ev, pointerIndex) - startY; // 1:1 scale delta
                }
                translateX = translateX + (deltaX / mScaleFactor);
                translateY = translateY + (deltaY / mScaleFactor);
                startX += deltaX;
                startY += deltaY;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                if( isScaling ){
                    int newIdx = ev.getActionIndex() == 0 ? 1 : 0; // The one not lifted
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newIdx);
                    startX = MotionEventCompat.getX(ev, newIdx);
                    startY = MotionEventCompat.getY(ev, newIdx);
                    isScaling = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = -1;
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
            float[] stationVectorCenter = station.getVectorCenter();
            if( Math.abs(stationVectorCenter[0] - x) < CLICK_THRESHOLD_DISTANCE &&
                Math.abs(stationVectorCenter[1] - y) < CLICK_THRESHOLD_DISTANCE ){

                Log.i(this.getClass().getSimpleName(), "MATCHED!!! "+Math.abs(stationVectorCenter[0] - x)+":"+Math.abs(stationVectorCenter[1] - y));
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
        this.setCenter(currentStation.getVectorCenter());
    }

    private void setCenter( float[] center ){
        mScaleFactor = 2.5f;
        this.scaleX = 0.f;
        this.scaleY = 0.f;
        this.translateX = -center[0] + (this.getWidth()/(2*mScaleFactor));
        this.translateY = -center[1] + (this.getHeight()/(2*mScaleFactor));
        invalidate();
    }

}
