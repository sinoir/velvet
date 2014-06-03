package com.delectable.mobile.ui.common.widget;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Draws an image in a circle/oval shape view.
 */
public class CircleImageView extends ImageView {

    private Paint mCircleShaderPaint;

    private RectF mContainerRectF;

    private BitmapShader mShader;

    private Bitmap mBitmap;

    private Target mPicassoTarget;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContainerRectF = new RectF();
        mCircleShaderPaint = new Paint();
        mCircleShaderPaint.setAntiAlias(true);

        if (getDrawable() != null) {
            mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mCircleShaderPaint.setShader(mShader);
        }
        setImageResource(0);

        // With Picasso , we need to use this target
        mPicassoTarget = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                updateShaderWithBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };
    }

    public Target getPicassoTarget() {
        return mPicassoTarget;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mContainerRectF.set(0, 0, w, h);
        if (mBitmap != null) {
            RectF bitmapSize = new RectF(0.0f, 0.0f, mBitmap.getWidth(), mBitmap.getHeight());
            Matrix matrix = new Matrix();
            matrix.setRectToRect(bitmapSize, mContainerRectF, Matrix.ScaleToFit.CENTER);
            mShader.setLocalMatrix(matrix);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawOval(mContainerRectF, mCircleShaderPaint);
        } else {
            super.onDraw(canvas);
        }
    }

    private void updateShaderWithBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        RectF bitmapSize = new RectF(0.0f, 0.0f, mBitmap.getWidth(), mBitmap.getHeight());
        Matrix matrix = new Matrix();

        if (mContainerRectF != null) {
            matrix.setRectToRect(bitmapSize, mContainerRectF, Matrix.ScaleToFit.CENTER);
        }
        mShader.setLocalMatrix(matrix);
        mCircleShaderPaint.setShader(mShader);
        invalidate();
    }
}
