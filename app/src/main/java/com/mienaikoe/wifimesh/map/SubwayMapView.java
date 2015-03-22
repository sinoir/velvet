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

    private TrainSystem system;
    private TrainStation currentStation;

    private float firstX = 0;
    private float firstY = -300;
    private float justX = firstX;
    private float justY = firstY;


    private float scaleFactor = 1.0f;
    private ScaleGestureDetector mScaleDetector;

    private Matrix transformer = new Matrix();

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

    private void init(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private static int WATER_COLOR = Color.parseColor("#82A4AD");
    private static int LAND_COLOR = Color.parseColor("#E3E2D5");

    private String[] STANDARD_GROUPS = new String[]{
            "TRAIN_LINES", "TRANSFERS", "STATION_BLOCKS", "TRAIN_NAMES", "STATION_NAMES",
    };

    private String[] CROSS_STREET_GROUPS = new String[]{
            "MANHATTAN_LAND", "CROSS_STREETS", "STREET_NAMES", "NEIGHBORHOOD_NAMES", "PARKS", "PARKS_TEXT"
    };

    private static Paint LOCATION_PAINT = new Paint();

    static {
        LOCATION_PAINT.setStrokeWidth(8.0f);
        LOCATION_PAINT.setColor(Color.parseColor("#ffffff"));
        LOCATION_PAINT.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        canvas.concat(this.transformer);

        if (this.showingStreets) {
            for (VectorInstruction instruction : this.crossStreetInstructions) {
                instruction.draw(canvas);
            }
            this.setBackgroundColor(WATER_COLOR);
        } else {
            this.setBackgroundColor(LAND_COLOR);
        }

        if (this.currentStation != null) {
            float[] stationVectorCenter = this.currentStation.getVectorCenter();
            canvas.drawArc(new RectF(
                    stationVectorCenter[0] - 24, stationVectorCenter[1] - 24,
                    stationVectorCenter[0] + 24, stationVectorCenter[1] + 24
            ), 0, 360, false, LOCATION_PAINT);
        }

        for (VectorInstruction instruction : this.mapInstructions) {
            instruction.draw(canvas);
        }

        canvas.restore();
    }


    private boolean showingStreets = true;

    public void toggleStreets() {
        showingStreets = !showingStreets;
        this.invalidate();
    }



    private static float MIN_SCALE_FACTOR = 0.5f;
    private static float MAX_SCALE_FACTOR = 6.0f;

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector mScaleDetector) {
            // Don't let the object get too small or too large.
            scaleFactor = scaleFactor * mScaleDetector.getScaleFactor();
            float matrixScaleFactor = mScaleDetector.getScaleFactor();
            if( scaleFactor < MIN_SCALE_FACTOR ){
                scaleFactor = MIN_SCALE_FACTOR;
                matrixScaleFactor = 1.f;
            } else if( scaleFactor > MAX_SCALE_FACTOR ){
                scaleFactor = MAX_SCALE_FACTOR;
                matrixScaleFactor = 1.f;
            }

            transformer.postScale(matrixScaleFactor, matrixScaleFactor, mScaleDetector.getFocusX(), mScaleDetector.getFocusY());
            return true;
        }
    }


    private boolean isScaling = false;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mScaleDetector.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                firstX = justX = event.getX();
                firstY = justY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if( !isScaling ) {
                    transformer.postTranslate(event.getX() - justX, event.getY() - justY);
                    justX = event.getX();
                    justY = event.getY();
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                isScaling = true;
                break;

            case MotionEvent.ACTION_UP:
                if( event.getX() == firstX && event.getY() == firstY ){
                    onClick(event.getX(), event.getY());
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) {
                    // This prevents hysteresis jitter when using two fingers
                    int newIdx = event.getActionIndex() == 0 ? 1 : 0; // The one not lifted
                    justX = MotionEventCompat.getX(event, newIdx);
                    justY = MotionEventCompat.getY(event, newIdx);
                    isScaling = false;
                }
                break;
        }

        invalidate();

        return true;
    }


    private int CLICK_THRESHOLD_DISTANCE = 20;

    private void onClick(float x, float y) {
        Matrix inverse = new Matrix();
        transformer.invert(inverse);
        float[] mapTouches = new float[]{ x, y};
        inverse.mapPoints(mapTouches);
        x = mapTouches[0];
        y = mapTouches[1];

        // much quicker would be to keep around two binary Trees sorted by coordinate and find intersections
        for (TrainStation station : system.getStations()) {
            float[] stationVectorCenter = station.getVectorCenter();
            if (Math.abs(stationVectorCenter[0] - x) < CLICK_THRESHOLD_DISTANCE && Math.abs(stationVectorCenter[1] - y) < CLICK_THRESHOLD_DISTANCE) {
                mEventBus.postSticky(new StationSelectEvent(station, false));
                return;
            }
        }
    }



    public void setSystem(TrainSystem system) {
        this.system = system;
    }

    public void setIngestor(VectorMapIngestor ingestor) {
        this.ingestor = ingestor;
        for (String groupName : CROSS_STREET_GROUPS) {
            if (this.ingestor.getInstructionGroup(groupName) == null) {
                continue;
            }
            this.crossStreetInstructions.addAll(this.ingestor.getInstructionGroup(groupName));
        }
        for (String groupName : STANDARD_GROUPS) {
            if (this.ingestor.getInstructionGroup(groupName) == null) {
                continue;
            }
            this.mapInstructions.addAll(this.ingestor.getInstructionGroup(groupName));
        }
    }

    public void onEventMainThread( StationSelectEvent event ){
        if (event == null) {
            return;
        }
        if( event.getStation().hasRectangles() ) {
            this.currentStation = event.getStation();
            if (event.isMoveTo()) {
                this.setCenter(currentStation.getVectorCenter());
            }
        } else {
            this.currentStation = null;
        }
    }

    private void setCenter(float[] center) {
        scaleFactor = 2.5f;
        transformer.reset();
        transformer.postTranslate(  -center[0]  + this.getCenterX(), -center[1]  + this.getCenterY() );
        transformer.postScale(scaleFactor, scaleFactor, this.getCenterX(), this.getCenterY() );
        invalidate();
    }

    private float getCenterX(){
        return this.getWidth() / 2;
    }

    private float getCenterY(){
        return this.getHeight() / 2;
    }


    public void catchup(SubwayMapView other){
        this.transformer = other.transformer;
    }
}
