package com.delectable.mobile.ui.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RatingsBarView extends View {

    private int mNumSections;

    private Paint mDividerPaint;

    private Paint mBackgroundPaint;

    private Paint mColorPaint;

    private RectF mBarRectF;

    private RectF mPercentRectF;

    private RectF mViewBounds;

    private float mSectionWidth;

    private float mColoredWidth;

    private float mPercent;

    public RatingsBarView(Context context) {
        this(context, null);
    }

    public RatingsBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingsBarView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mNumSections = 4;
        mPercent = 0.0f;

        mViewBounds = new RectF();

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(0xFFDEDEDE);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);

        // TODO: Dynamic color based on %
        mColorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorPaint.setColor(0xFF7BCC2F);
        mColorPaint.setStyle(Paint.Style.STROKE);

        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(Color.WHITE);
        mDividerPaint.setStyle(Paint.Style.STROKE);
        mDividerPaint.setStrokeWidth(2.0f);

        setupFakeData();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewBounds = new RectF(0.0f, 0.0f, w, h);
        mSectionWidth = mNumSections > 0 ? w / mNumSections : w;

        mBackgroundPaint.setStrokeWidth(h * 2);
        mColorPaint.setStrokeWidth(h * 2);

        setPercent(mPercent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw gray background
        canvas.drawLine(0, 0, mViewBounds.width(), 0, mBackgroundPaint);

        // draw colored guage
        canvas.drawLine(0, 0, mColoredWidth, 0, mColorPaint);

        // draw Dividers
        for (int i = 1; i < mNumSections; i++) {
            float sectionStart = i * mSectionWidth;
            canvas.drawLine(sectionStart, 0, sectionStart, mViewBounds.height(), mDividerPaint);
        }
    }

    public void setPercent(float percent) {
        mPercent = Math.min(percent, 1.0f);

        if (mViewBounds.width() > 0) {
            mColoredWidth = mPercent * mViewBounds.width();
        }
        mColorPaint.setStrokeWidth(mColoredWidth);
        mColorPaint.setColor(colorForPercent());
        invalidate();
    }

    public int colorForPercent() {
        // TODO: Tweek up the values
        float hue = mPercent * 130.0f;
        float saturation = 0.80f;
        float value = 0.80f;

        float[] hsv = new float[] {
                hue,
                saturation,
                value,
        };
        return Color.HSVToColor(hsv);
    }

    /**
     * For previewing in UI editor
     */
    private void setupFakeData() {
        if (isInEditMode()) {
            setPercent(1.28f);
        }
    }
}
