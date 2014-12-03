package com.delectable.mobile.util;

import com.delectable.mobile.App;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Convenience API for animating views into different states.
 */
public class Animate {

    public static final int SHORT = 200;

    public static final int MEDIUM = 400;

    public static final int LONG = 600;

    public static final int TRANSLATION = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75,
                    App.getInstance().getResources().getDisplayMetrics());

    public static final int TRANSLATION_SMALL = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42,
                    App.getInstance().getResources().getDisplayMetrics());

    private static final Interpolator ACCELERATE = new AccelerateInterpolator();

    private static final Interpolator DECELERATE = new DecelerateInterpolator();

    private static final Interpolator OVERSHOOT = new OvershootInterpolator();

    public static void slideOutDown(final View view) {
        slideOutDown(view, 0);
    }

    public static void slideOutDown(final View view, long startDelay) {
        view.animate()
                .translationY(view.getMeasuredHeight())
                .setDuration(MEDIUM)
                .setStartDelay(startDelay)
                .setInterpolator(ACCELERATE)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void slideInUp(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .translationY(0)
                .setDuration(LONG)
                .setInterpolator(DECELERATE)
                .setListener(null)
                .start();
    }

    public static void pushOutUp(final View view) {
        view.animate()
                .alpha(0)
                .translationY(-TRANSLATION)
                .setDuration(MEDIUM)
                .setInterpolator(ACCELERATE)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void pushInDown(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .translationY(0)
                .setDuration(MEDIUM)
                .setInterpolator(DECELERATE)
                .setListener(null)
                .start();
    }

    public static void pushOutRight(final View view) {
        view.animate()
                .alpha(0)
                .translationX(TRANSLATION)
                .setDuration(MEDIUM)
                .setInterpolator(ACCELERATE)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void pushInLeft(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .translationX(0)
                .setDuration(MEDIUM)
                .setInterpolator(DECELERATE)
                .setListener(null)
                .start();
    }

    public static void rollOutRight(final View view) {
        view.animate()
                .alpha(0)
                .translationX(TRANSLATION)
                .rotation(180)
                .setDuration(MEDIUM)
                .setInterpolator(ACCELERATE)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void rollInRight(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .translationX(0)
                .rotation(0)
                .setDuration(MEDIUM)
                .setInterpolator(DECELERATE)
                .setListener(null)
                .start();
    }

    public static void rollInLeft(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .translationX(0)
                .rotation(0)
                .setDuration(MEDIUM)
                .setInterpolator(DECELERATE)
                .setListener(null)
                .start();
    }

    public static void rollOutLeft(final View view) {
        view.animate()
                .alpha(0)
                .translationX(-TRANSLATION)
                .rotation(-180)
                .setDuration(MEDIUM)
                .setInterpolator(ACCELERATE)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void fadeIn(final View view) {
        fadeIn(view, 0);
    }

    public static void fadeIn(final View view, long startDelay) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .setDuration(MEDIUM)
                .setStartDelay(startDelay)
                .setListener(null)
                .start();
    }

    public static void fadeOut(final View view) {
        fadeOut(view, 0);
    }

    public static void fadeOut(final View view, long startDelay) {
        view.animate()
                .alpha(0)
                .setDuration(LONG)
                .setStartDelay(startDelay)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }

    public static void crossfadeRotate(final View from, final View to) {
        rotateOut(from);
        rotateIn(to);
    }

    public static void crossfadeRotate(final View from, final View to, boolean revert) {
        if (revert) {
            crossfadeRotateRevert(to, from);
        } else {
            crossfadeRotate(from, to);
        }
    }

    public static void crossfadeRotateRevert(final View from, final View to) {
        rotateOutRevert(from);
        rotateInRevert(to);
    }

    public static void rotateIn(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setRotation(-180);
        view.animate()
                .alpha(1)
                .rotation(0)
                .setDuration(MEDIUM)
                .setListener(null)
                .start();
    }

    public static void rotateOut(final View view) {
        view.animate()
                .alpha(0)
                .rotation(180)
                .setDuration(MEDIUM)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }

    public static void rotateInRevert(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .rotation(0)
                .setDuration(MEDIUM)
                .setListener(null)
                .start();
    }

    public static void rotateOutRevert(final View view) {
        view.animate()
                .alpha(0)
                .rotation(-180)
                .setDuration(MEDIUM)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }

    public static void grow(final View view) {
        grow(view, 0);
    }

    public static void grow(final View view, long startDelay) {
        view.setScaleX(0);
        view.setScaleY(0);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setDuration(LONG)
                .setStartDelay(startDelay)
                .setInterpolator(OVERSHOOT)
                .setListener(null)
                .start();
    }

}
