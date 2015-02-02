package com.mienaikoe.wifimesh;

import com.mienaikoe.wifimesh.train.TrainLine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * TODO: document your custom view class.
 */
public class TrainLineIcon extends TypefaceTextView {

    private static final int DEFAULT = -1;

    private Paint mPainter = new Paint();

    private int size;

    public TrainLineIcon(Context context) {
        super(context);
    }


    public TrainLineIcon(Context context, AttributeSet attrs) {
        super(context);
        init(attrs);
    }

    public TrainLineIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {


        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TrainLineIcon);

        int defaultSize = getResources().getDimensionPixelSize(R.dimen.train_icon_small);
        size = a.getDimensionPixelSize(R.styleable.TrainLineIcon_iconSize, defaultSize);
        a.recycle();

        if (isInEditMode()) {
            return;
        }

        setTypeface(FontEnum.HELVETICA_NEUE_MEDIUM);
        //setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    public void setData(String text, int iconColor, int textColor, int pixelSize) {
        //keep current size if default option was provided
        if (pixelSize == DEFAULT) {
            pixelSize = size;
        }
        size = pixelSize;
        setText(text);
        setTextSize(pixelSize
                * 0.2F); // What are the parameter units?? Causing weird sizing on different devices
        setHeight(pixelSize);
        setWidth(pixelSize);
        mPainter.setColor(getResources().getColor(iconColor));
        setTextColor(getResources().getColor(textColor));
        invalidate();
    }

    /**
     * Convenience method to set TrainLine to view.
     */
    public void setTrainLine(TrainLine line, int size){
        setData(line.getName(),
                line.getBackgroundColor(),
                line.getForegroundColor(),
                size);
    }

    public void setTrainLine(TrainLine line){
        setData(line.getName(),
                line.getBackgroundColor(),
                line.getForegroundColor(),
                DEFAULT);
    }





    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.drawCircle(width / 2, height/ 2, this.size / 2, mPainter);
        setGravity(Gravity.CENTER);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        super.onDraw(canvas);
    }

}
