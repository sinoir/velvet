package com.delectable.mobile.ui.common.drawable;

import com.delectable.mobile.R;
import com.delectable.mobile.ui.common.widget.Rating;
import com.delectable.mobile.util.ColorsUtil;

import android.content.Context;
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

    private Context mContext;

    public RatingsBar(Context context, float barHeight) {
        mContext = context;
        mBarHeight = barHeight;
        mPercent = 0.0f;
        mCornerRadius = 2.0f;

        int lightGray = mContext.getResources().getColor(R.color.d_light_gray);
        mDefaultBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultBarPaint.setColor(lightGray);
        mDefaultBarPaint.setStyle(Paint.Style.FILL);

        mColorOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorOverlayPaint.setColor(lightGray); //doesn't really matter what this is set to
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
        canvas.drawRoundRect(
                barRectWithWidth(width),
                mCornerRadius,
                mCornerRadius,
                paint);
    }

    private void drawDividers(Canvas canvas) {
        RectF baseRect = barRectWithWidth(getBounds().width());
        for (int i = 1; i < sNumSections; i++) {
            float sectionStart = i * mSectionWidth;
            canvas.drawLine(sectionStart,
                    baseRect.top,
                    sectionStart,
                    baseRect.bottom,
                    mDividerPaint);
        }
    }

    private RectF barRectWithWidth(float width) {
        float centerY = getBounds().centerY();
        float halfHeight = mBarHeight / 2.0f;
        float bottom = halfHeight + centerY;
        float top = centerY - halfHeight;
        float left = getBounds().left;
        float right = getBounds().left + width;

        return new RectF(left, top, right, bottom);
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
        Rating rating = Rating.valueForPercentage(mPercent);
        return mContext.getResources().getColor(rating.getColorRes());
    }
}
