package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainLine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.Gravity;

/**
 * TODO: document your custom view class.
 */
public class TrainLineIcon extends TypefaceTextView {

    private TrainLine line;
    private int size;



    public TrainLineIcon(Context context) {
        super(context);
    }

    public TrainLineIcon(Context context, TrainLine line, int size){
        super(context);
        this.line = line;
        this.size = size;
        this.setLayout();
    }



    private void setLayout(){
        setTypeface(FontEnum.HELVETICA_NEUE_MEDIUM);

        this.setText(this.line.getName());
        this.setHeight(size);
        this.setWidth(size);

        this.setGravity(Gravity.CENTER);
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setTextSize(this.size * 0.2F); // What are the parameter units?? Causing weird sizing on different devices

        this.setTextColor(getResources().getColor(this.line.getForegroundColor()));
        this.setBackgroundColor(getResources().getColor(R.color.transparent));
    }



    @Override
    protected void onDraw(Canvas canvas) {
        Paint painter = new Paint();
        painter.setColor(getResources().getColor(this.line.getBackgroundColor()));
        canvas.drawCircle(this.size/2, this.size/2, this.size/2, painter);

        super.onDraw(canvas);
    }

}
