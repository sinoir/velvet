package com.mienaikoe.wifimesh.map;

import com.mienaikoe.wifimesh.StationSelectEvent;
import com.mienaikoe.wifimesh.train.TrainStation;
import com.mienaikoe.wifimesh.train.TrainSystem;

import android.content.Context;
import android.gesture.GestureOverlayView;
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

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * TODO: document your custom view class.
 */
public class SubwayMapView extends View {

    private GestureOverlayView aps;

    private TrainSystem system;
    private TrainStation currentStation;

    private float startX = 0;
    private float startY = 0;
    private float translateX = 0;
    private float translateY = -300;

    private float scaleFactor = 1.0f;
    private float startScaleFactor = 1.0f;
    private float scaleScreenX = 0.f;
    private float scaleScreenY = 0.f;
    private ScaleGestureDetector mScaleDetector;




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
    private static Paint TEST_PAINT2 = new Paint();
    static{
        TEST_PAINT2.setColor(Color.parseColor("#447700"));
        TEST_PAINT2.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Yay Matrix Math
        Matrix transformationMatrix = new Matrix();

        Log.i("SCALE:     ", scaleFactor + "");
        Log.i("TRANSLATE: ", translateX + "," + translateY);

        transformationMatrix.setTranslate(translateX / scaleFactor, translateY / scaleFactor);
        transformationMatrix.postScale(scaleFactor, scaleFactor, scaleScreenX, scaleScreenY);
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

    }


    private boolean showingStreets = true;

    public void toggleStreets() {
        showingStreets = !showingStreets;
        this.invalidate();
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor * detector.getScaleFactor(), 6.0f));
            scaleScreenX = detector.getFocusX();
            scaleScreenY = detector.getFocusY();

            return true;
        }
    }





    private int mActivePointerId = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        mScaleDetector.onTouchEvent(ev);

        if (ev.getPointerCount() > 1) {
            //calculateScalePoint(ev);
        }

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                startX = (MotionEventCompat.getX(ev, 0) - translateX);
                startY = (MotionEventCompat.getY(ev, 0) - translateY);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                mActivePointerId = -1;
                startX = scaleScreenX - translateX;
                startY = scaleScreenY - translateY;
                startScaleFactor = scaleFactor;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (ev.getPointerCount() == 1) {
                    final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                    if (pointerIndex == -1) {
                        mActivePointerId = -1;
                        break;
                    }
                    translateX = (MotionEventCompat.getX(ev, pointerIndex) - startX);
                    translateY = (MotionEventCompat.getY(ev, pointerIndex) - startY);
                } else {
                    translateX = (scaleScreenX - startX);
                    translateY = (scaleScreenY - startY);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                if (ev.getPointerCount() == 2) {
                    int newIdx = ev.getActionIndex() == 0 ? 1 : 0; // The one not lifted
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newIdx);

                    startX = (MotionEventCompat.getX(ev, newIdx)) - translateX;
                    startY = (MotionEventCompat.getY(ev, newIdx)) - translateY;
                    startScaleFactor = scaleFactor;
                } else if (ev.getPointerCount() > 2) {
                    startX = (scaleScreenX) - translateX;
                    startY = (scaleScreenY) - translateY;
                    startScaleFactor = scaleFactor;
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
            if (this.ingestor.getInstructionGroup(groupName) == null) {
                continue;
            }
            this.crossStreetInstructions.addAll(this.ingestor.getInstructionGroup(groupName));
        }
        for( String groupName : STANDARD_GROUPS ) {
            if (this.ingestor.getInstructionGroup(groupName)==null) {
                continue;
            }
            this.mapInstructions.addAll(this.ingestor.getInstructionGroup(groupName));
        }
    }

    public void setStation(TrainStation currentStation) {
        this.currentStation = currentStation;
        this.setCenter(currentStation.getVectorCenter());
    }

    private void setCenter( float[] center ){
        scaleFactor = 2.5f;
        this.scaleScreenX = (this.getWidth() / 2) / scaleFactor;
        this.scaleScreenY = (this.getHeight() / 2) / scaleFactor;
        this.translateX = -center[0];
        this.translateY = -center[1];
        invalidate();
    }

}
