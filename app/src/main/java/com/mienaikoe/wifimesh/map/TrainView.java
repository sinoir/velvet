package com.mienaikoe.wifimesh.map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
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

    public TrainView(Context context) {
        super(context);
    }

    public TrainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        double offsetLatitude = 40.95; // top-side latitude limit
        double offsetLongitude = -74.28; // left-side longitude limit

        float multiplierX = 1900;
        float multiplierY = 2900;

        Paint painter = new Paint();
        painter.setColor(getResources().getColor(R.color.black));

        for(TrainStation station : this.system.getStations()){
            canvas.drawCircle(
                    (float)(station.getLongitude() - offsetLongitude) * multiplierX,
                    (float)(offsetLatitude - station.getLatitude()) * multiplierY,
                    5.0F,
                    painter );
        }

        super.onDraw(canvas);
    }

    public void setSystem(TrainSystem system) {
        this.system = system;
    }

    public void setStation(TrainStation currentStation) {
        this.currentStation = currentStation;
    }
}
