package com.mienaikoe.wifimesh.train;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Jesse on 1/18/2015.
 */
public class StationLayout extends LinearLayout {

    private Context context;
    private ScaleGestureDetector mScaleDetector;

    private int bottomCapMargin;
    private int topCapMargin;
    private int targetMargin;

    private int upMargin;

    private StationFillingAnimation fillingAnimation;

    private float firstY;
    private float distY;





    public StationLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        this.bottomCapMargin = (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, -40, context.getResources().getDisplayMetrics() );
        this.targetMargin = this.bottomCapMargin;
    }

    public StationLayout(Context context){
        this(context, null);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if( this.topCapMargin == 0 ){
            this.setTopMargin();
        }

        mScaleDetector.onTouchEvent(ev); // TODO: Needed?
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if( this.fillingAnimation != null ){
                    this.fillingAnimation.cancel();
                }
                firstY = ev.getRawY();
                break;
            } case MotionEvent.ACTION_MOVE: {
                final float y = ev.getRawY();
                distY = firstY - y;
                invalidate(this.targetMargin - (int) distY);
                break;
            } case MotionEvent.ACTION_UP: {
                this.upMargin = this.targetMargin - (int)distY;
                if( distY > 100 && this.targetMargin == bottomCapMargin ){
                    this.targetMargin = topCapMargin;
                } else if( distY < -100 && this.targetMargin == topCapMargin ){
                    this.targetMargin = bottomCapMargin;
                }
                this.fillingAnimation = new StationFillingAnimation(this);
                this.fillingAnimation.setDuration(300);
                this.startAnimation(this.fillingAnimation);
            }
        }
        return true;
    }

    public void invalidate(int newTopMargin){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)this.getLayoutParams();
        params.topMargin = Math.min(Math.max(newTopMargin, this.topCapMargin), this.bottomCapMargin);
        this.setLayoutParams(params);
        super.invalidate();
    }


    private void setTopMargin(){
        this.topCapMargin = - ((RelativeLayout)this.getParent()).getMeasuredHeight();
    }





    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mScaleFactor = 1.0F;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }



    private class StationFillingAnimation extends Animation {

        private StationLayout stationLayout;
        private int distance;

        StationFillingAnimation(StationLayout view){
            this.stationLayout = view;
            this.distance = targetMargin - upMargin;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newTopMargin = (int) (targetMargin - (this.distance * (1.0-interpolatedTime)));
            stationLayout.invalidate(newTopMargin);
        }
    };
}
