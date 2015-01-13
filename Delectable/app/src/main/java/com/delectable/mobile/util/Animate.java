package com.delectable.mobile.util;

import com.delectable.mobile.App;
import com.delectable.mobile.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Convenience API for animating views into different states.
 */
public class Animate {

    public static final int SHORT = 250;

    public static final int MEDIUM = 400;

    public static final int LONG = 600;

    public static final int TRANSLATION = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75,
                    App.getInstance().getResources().getDisplayMetrics());

    public static final int TRANSLATION_SMALL = (int) TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,
                    App.getInstance().getResources().getDisplayMetrics());

    public static final float ELEVATION = App.getInstance().getResources().getDimension(R.dimen.tab_elevation);

    private static final Interpolator ACCELERATE = new AccelerateInterpolator();

    private static final Interpolator DECELERATE = new DecelerateInterpolator();

    private static final Interpolator ACCELERATE_DECELERATE
            = new AccelerateDecelerateInterpolator();

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

    public static void fadeInVertical(final View view) {
        fadeTranslateIn(view, 0, TRANSLATION_SMALL, 0);
    }

    public static void fadeInVertical(final View view, long startDelay) {
        fadeTranslateIn(view, 0, TRANSLATION_SMALL, startDelay);
    }

    public static void fadeInHorizontal(final View view) {
        fadeTranslateIn(view, TRANSLATION_SMALL, 0, 0);
    }

    public static void fadeInHorizontal(final View view, long startDelay) {
        fadeTranslateIn(view, TRANSLATION_SMALL, 0, startDelay);
    }

    public static void fadeTranslateIn(final View view, int translationX, int translationY,
            long startDelay) {
        view.setTranslationX(translationX);
        view.setTranslationY(translationY);
        view.setVisibility(View.VISIBLE);
        view.animate()
                .alpha(1)
                .translationX(0)
                .translationY(0)
                .setDuration(SHORT)
                .setStartDelay(startDelay)
                .setInterpolator(DECELERATE)
                .setListener(null)
                .start();
    }

    public static void fadeOutVertical(final View view) {
        fadeTranslateOut(view, 0, TRANSLATION_SMALL, 0);
    }

    public static void fadeOutVertical(final View view, long startDelay) {
        fadeTranslateOut(view, 0, TRANSLATION_SMALL, startDelay);
    }

    public static void fadeOutHorizontal(final View view) {
        fadeTranslateOut(view, TRANSLATION_SMALL, 0, 0);
    }

    public static void fadeOutHorizontal(final View view, long startDelay) {
        fadeTranslateOut(view, TRANSLATION_SMALL, 0, startDelay);
    }

    public static void fadeTranslateOut(final View view, int translationX, int translationY,
            long startDelay) {
        view.animate()
                .alpha(0)
                .translationX(translationX)
                .translationY(translationY)
                .setDuration(SHORT)
                .setStartDelay(startDelay)
                .setInterpolator(DECELERATE)
                .setListener(null)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        view.setVisibility(View.INVISIBLE);
//                    }
//                })
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

    public static void shrink(final View view) {
        shrink(view, 0);
    }

    public static void shrink(final View view, long startDelay) {
        view.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setDuration(LONG)
                .setStartDelay(startDelay)
                .setInterpolator(OVERSHOOT)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    public static void shrinkAndGrow(final View view) {
        shrinkAndGrow(view, 0);
    }

    public static void shrinkAndGrow(final View view, final long startDelay) {
        view.animate()
                .alpha(0)
                .scaleX(0)
                .scaleY(0)
                .setDuration(LONG)
                .setStartDelay(startDelay)
                .setInterpolator(OVERSHOOT)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Animate.grow(view);
                    }
                })
                .start();
    }

    public static void elevate(final View view) {
        elevate(view, ELEVATION, 0);
    }

    public static void elevate(final View view, final long startDelay) {
        elevate(view, ELEVATION, startDelay);
    }

    public static void elevate(final View view, final float elevation) {
        elevate(view, elevation, 0);
    }

    public static void elevate(final View view, final float elevation, final long startDelay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.animate()
                    .z(elevation)
                    .setDuration(MEDIUM)
                    .setStartDelay(startDelay)
                    .setListener(null)
                    .start();
        } else {
            ViewCompat.setElevation(view, elevation);
        }
    }

    public static void circularReveal(final View view) {
        circularReveal(view, -1, -1, 0);
    }

    public static void circularReveal(final View view, final long startDelay) {
        circularReveal(view, -1, -1, startDelay);
    }

    public static void circularReveal(final View view, int centerX, int centerY) {
        circularReveal(view, centerX, centerY, 0);
    }

    public static void circularReveal(final View view, int centerX, int centerY, final long startDelay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(view.getWidth(), view.getHeight());

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(
                    view,
                    centerX < 0 ? cx : centerX,
                    centerY < 0 ? cy : centerY,
                    0,
                    finalRadius);
            anim.setStartDelay(startDelay);

            view.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void circularHide(final View view) {
        circularHide(view, 0);
    }

    public static void circularHide(final View view, final long startDelay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;

            // get the initial radius for the clipping circle
            int initialRadius = view.getWidth();

            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
            anim.setStartDelay(startDelay);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            anim.start();
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
