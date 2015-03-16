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
    private float previousX = translateX;
    private float previousY = translateY;

    private float scaleFactor = 1.0f;
    private float scaleScreenX = 0.f;
    private float scaleScreenY = 0.f;
    private float previousFocusX = 0.f;
    private float previousFocusY = 0.f;
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

    private static Paint TEST_PAINT = new Paint();

    static {
        TEST_PAINT.setColor(Color.parseColor("#000000"));
        TEST_PAINT.setStyle(Paint.Style.FILL);
    }

    private static Paint TEST_PAINT2 = new Paint();

    static {
        TEST_PAINT2.setColor(Color.parseColor("#447700"));
        TEST_PAINT2.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        float displayWidth = this.getWidth() * scaleFactor;
        float displayHeight = this.getHeight() * scaleFactor;

        float realTranslateX = translateX / scaleFactor;
        float realTranslateY = translateY / scaleFactor;

        //We're going to scale the X and Y coordinates by the same amount
        canvas.scale(scaleFactor, scaleFactor, scaleScreenX, scaleScreenY);

        //We need to divide by the scale factor here, otherwise we end up with excessive panning based on our zoom level
        //because the translation amount also gets scaled according to how much we've zoomed into the canvas.
        canvas.translate( realTranslateX, realTranslateY);

        Log.i("SCALE:     ", scaleFactor + "");
        Log.i("TRANSLATE: ", translateX + "," + translateY);

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


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector mScaleDetector) {
            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor * mScaleDetector.getScaleFactor(), 6.0f));

            return true;
        }
    }


    private int mActivePointerId = -1;


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mScaleDetector.onTouchEvent(event);
        if (mScaleDetector.isInProgress()) {
            previousFocusX = mScaleDetector.getFocusX();
            previousFocusY = mScaleDetector.getFocusY();
        }
        scaleScreenX =  previousFocusX;
        scaleScreenY =  previousFocusY;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                //We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
                //amount for each coordinates This works even when we are translating the first time because the initial
                //values for these two variables is zero.
                startX = event.getX() - previousX;
                startY = event.getY() - previousY;
                break;

            case MotionEvent.ACTION_MOVE:
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                //All fingers went up, so let's save the value of translateX and translateY into previousX and
                //previousTranslate
                previousX = translateX;
                previousY = translateY;
                break;

            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) {
                    // This prevents hysteresis jitter when using two fingers
                    int newIdx = event.getActionIndex() == 0 ? 1 : 0; // The one not lifted
                    mActivePointerId = MotionEventCompat.getPointerId(event, newIdx);
                    startX = (MotionEventCompat.getX(event, newIdx)) - translateX;
                    startY = (MotionEventCompat.getY(event, newIdx)) - translateY;

                    // This makes it so there's no jitter when you do multiple zoom gestures
                    /*
                    float scaleVectorX = (scaleScreenX*scaleFactor) - scaleScreenX;
                    float scaleVectorY = (scaleScreenY*scaleFactor) - scaleScreenY;
                    startX = startX - scaleVectorX;
                    startY = startY - scaleVectorY;
                    translateX = translateX - scaleVectorX;
                    translateY = translateY - scaleVectorY;
                    scaleScreenX = 0;
                    scaleScreenY = 0;
                    /**/
                }

                //This is not strictly necessary; we save the value of translateX and translateY into previousX
                //and previousY when the second finger goes up
                previousX = translateX;
                previousY = translateY;
                break;
        }

        invalidate();

        return true;
    }


    private static final int CLICK_THRESHOLD_DISTANCE = 20;

    private float clickedX;
    private float clickedY;

    private void onClick(float x, float y) {
        Log.i(this.getClass().getSimpleName(), "On Clicking: [" + x + "," + y + "]");
        clickedX = x;
        clickedY = y;
        for (TrainStation station : this.system.getStations()) {
            float[] stationVectorCenter = station.getVectorCenter();
            if (Math.abs(stationVectorCenter[0] - x) < CLICK_THRESHOLD_DISTANCE &&
                    Math.abs(stationVectorCenter[1] - y) < CLICK_THRESHOLD_DISTANCE) {

                Log.i(this.getClass().getSimpleName(), "MATCHED!!! " + Math.abs(stationVectorCenter[0] - x) + ":" + Math.abs(stationVectorCenter[1] - y));
                Log.i(this.getClass().getSimpleName(), "MATCHED!!! " + station.getName());

                mEventBus.postSticky(new StationSelectEvent(station));
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

    public void setStation(TrainStation currentStation) {
        this.currentStation = currentStation;
        this.setCenter(currentStation.getVectorCenter());
    }

    private void setCenter(float[] center) {
        scaleFactor = 2.5f;
        this.scaleScreenX = (this.getWidth() / 2) / scaleFactor;
        this.scaleScreenY = (this.getHeight() / 2) / scaleFactor;
        this.translateX = -center[0];
        this.translateY = -center[1];
        invalidate();
    }


    private void catchup(SubwayMapView other){
        this.translateX = other.translateX;
        this.translateY = other.translateY;
        this.scaleFactor = other.scaleFactor;
        this.previousFocusX = other.previousFocusX;
        this.previousFocusY = other.previousFocusY;
        this.previousX = other.previousX;
        this.previousY = other.previousY;
    }
}
