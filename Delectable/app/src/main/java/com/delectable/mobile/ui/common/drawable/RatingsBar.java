package com.delectable.mobile.ui.common.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class RatingsBar extends Drawable {

    private static final int sNumSections = 4;

    private Paint mDefaultBarPaint;

    private Paint mColorOverlayPaint;

    private Paint mDividerPaint;

    private int mSectionWidth;

    private float mBarHeight;

    private float mPercent;

    private float mCornerRadius;

    public RatingsBar(float barHeight) {
        mBarHeight = barHeight;
        mPercent = 0.0f;
        mCornerRadius = 2.0f;

        mDefaultBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultBarPaint.setColor(0xFFDEDEDE);
        mDefaultBarPaint.setStyle(Paint.Style.FILL);

        mColorOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorOverlayPaint.setColor(0xFF7BCC2F);
        mColorOverlayPaint.setStyle(Paint.Style.FILL);

        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDividerPaint.setColor(Color.WHITE);
        mDividerPaint.setStyle(Paint.Style.STROKE);
        mDividerPaint.setStrokeWidth(2.0f);
    }

    @Override
    public void setAlpha(int alpha) {
        mDefaultBarPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mDefaultBarPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void draw(Canvas canvas) {
        mSectionWidth = getBounds().width() / sNumSections;

        drawGrayBar(canvas);
        drawColorOverlayBar(canvas);
        drawDividers(canvas);
    }

    private void drawGrayBar(Canvas canvas) {
        drawBarWithPaintAndWidth(canvas, getBounds().width(), mDefaultBarPaint);
    }

    private void drawColorOverlayBar(Canvas canvas) {
        if (mPercent > 0.0f) {
            drawBarWithPaintAndWidth(canvas, overlayColoredBarWidth(), mColorOverlayPaint);
        }
    }

    private void drawBarWithPaintAndWidth(Canvas canvas, float width, Paint paint) {
        float centerY = getBounds().centerY();
        float halfHeight = mBarHeight / 2.0f;
        float bottom = halfHeight + centerY;
        float top = centerY - halfHeight;
        float left = getBounds().left;
        float right = getBounds().left + width;

        RectF rectF = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, paint);
    }

    private void drawDividers(Canvas canvas) {
        for (int i = 1; i < sNumSections; i++) {
            float sectionStart = i * mSectionWidth;
            canvas.drawLine(sectionStart,
                    getBounds().top,
                    sectionStart,
                    getBounds().bottom,
                    mDividerPaint);
        }
    }

    private float overlayColoredBarWidth() {
        return mPercent * getBounds().width();
    }

    public void setPercent(float percent) {
        mPercent = Math.min(percent, 1.0f);
        mColorOverlayPaint.setColor(colorForPercent());
    }

    public void setBarHeight(float height) {
        mBarHeight = height;
    }

    public int colorForPercent() {
        float hue = mPercent * 130.0f;
        float saturation = 0.80f;
        float value = 0.80f;

        float[] hsv = new float[]{
                hue,
                saturation,
                value,
        };
        return Color.HSVToColor(hsv);
    }
}
