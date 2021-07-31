package com.tambyy.fanoronaakalana.graphics.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tambyy.fanoronaakalana.graphics.anim.Animation;
import com.tambyy.fanoronaakalana.graphics.anim.AnimationsManager;
import com.tambyy.fanoronaakalana.graphics.drawable.Drawable;
import com.tambyy.fanoronaakalana.graphics.drawable.Touchable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SceneView extends View {

    /**
     * Drawables to draw into the canvas
     */
    private final List<Drawable> drawables = new ArrayList<>();

    /**
     * a kind of locking
     * allowing to check that one is drawing in the canvas
     */
    private boolean drawing = false;

    /**
     * Allowing to lock the canvas
     * so that there is no interaction we can do on the canvas when touched
     */
    private boolean touchable = true;

    /**
     * Animations manager
     */
    private AnimationsManager animationsManager = new AnimationsManager();

    /**
     * Animation speed
     */
    private float animationSpeed = 1f;

    /**
     * Animation running
     */
    private boolean animationRunning = true;

    /**
     *
     * @param context
     */
    public SceneView(Context context) {
        super(context);
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public SceneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SceneView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AnimationsManager getAnimationsManager() {
        return animationsManager;
    }

    /**
     * Redraw all drawables inside the canvas
     */
    public void draw() {
        // Avoid drawing on the same canvas
        // in different threads simultaneously
        if (!drawing) {
            drawing = true;
            // it will call onDraw method
            this.postInvalidate();
        }
    }

    /**
     * reorder the drawables with respect to their z index
     * we first plot the drawbles with a low z index
     * and then those with a much higher z index
     */
    private void reorderDrawablesByZIndex() {
        Collections.sort(drawables, new Comparator<Drawable>() {
            @Override
            public int compare(Drawable drawable1, Drawable drawable2) {
                return drawable1.getZ() < drawable2.getZ() ? -1 : 1;
            }
        });
    }

    /**
     * Add item to draw into the canvas
     *
     * @param drawable
     *        drawable to add
     */
    public void addDrawable(Drawable drawable) {
        synchronized (this.drawables) {
            if (!drawables.contains(drawable)) {
                drawables.add(drawable);
                drawable.setParent(this);
                reorderDrawablesByZIndex();
                draw();
            }
        }
    }

    /**
     * Add many items to draw into the canvas
     *
     * @param drawables
     *        drawables to
     */
    public void addDrawables(Collection<? extends Drawable> drawables) {
        synchronized (this.drawables) {
            for (Drawable drawable: drawables) {
                if (!this.drawables.contains(drawable)) {
                    this.drawables.add(drawable);
                    drawable.setParent(this);
                }
            }

            reorderDrawablesByZIndex();
            draw();
        }
    }

    /**
     * Add item to draw into the canvas
     *
     * @param drawable
     *        drawable to add
     */
    public void removeDrawable(Drawable drawable) {
        synchronized (this.drawables) {
            drawables.remove(drawable);
            draw();
        }
    }

    /**
     * Add many items to draw into the canvas
     *
     * @param drawables
     *        drawables to
     */
    public void removeDrawables(Collection<? extends Drawable> drawables) {
        synchronized (this.drawables) {
            this.drawables.removeAll(drawables);
            draw();
        }
    }

    /**
     * Remove all drawables
     */
    public void clear() {
        synchronized (this.drawables) {
            this.animationsManager.clear();
            this.drawables.clear();
            draw();
        }
    }

    /**
     *
     */
    public void clearAnimations() {
        this.animationsManager.clear();
    }

    /**
     * Add animation
     * @param animation
     */
    public void addAnimation(Animation animation) {
        animationsManager.addAnimation(animation);
        draw();
    }

    /**
     *
     */
    public void startAnimation() {
        this.animationRunning = true;
        this.drawing = false;
        draw();
    }

    /**
     *
     */
    public static interface AnimationPausedListener {
        public void onAnimationPaused();
    }

    /**
     *
     */
    private AnimationPausedListener animationPausedListener = null;

    /**
     *
     */
    public void pauseAnimation() {
        this.pauseAnimation(null);
    }

    /**
     *
     */
    public void pauseAnimation(AnimationPausedListener animationPausedListener) {
        this.animationPausedListener = animationPausedListener;
        this.animationRunning = false;

        if (!drawing && this.animationPausedListener != null) {
            this.animationPausedListener.onAnimationPaused();
            this.animationPausedListener = null;
        }
    }

    /**
     *
     * @return
     */
    public boolean isAnimationRunning() {
        return animationRunning;
    }

    /**
     *
     * @param animationSpeed
     */
    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    /**
     *
     */
    public float getAnimationSpeed() {
        return animationSpeed;
    }

    /**
     * All drawables inside this view
     * will be drawn by this method
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        synchronized (drawables) {

            if (
                animationRunning &&
                animationsManager.advance((int) (animationSpeed * AnimationsManager.ANIMATION_TIME_INTERVAL))
            ) {
                postInvalidateDelayed(AnimationsManager.ANIMATION_TIME_INTERVAL);
            } else {
                if (!animationRunning && animationPausedListener != null) {
                    animationPausedListener.onAnimationPaused();
                    animationPausedListener = null;
                }

                drawing = false;
            }

            // We draw each drawable one after another
            synchronized (drawables) {
                for (Drawable drawable : drawables) {
                    // save canvas state
                    canvas.save();

                    // translate canvas to the drawable position
                    final float scale = drawable.getScale();
                    final float xpos = drawable.getX() + (1f - scale) * drawable.getWidth() / 2;
                    final float ypos = drawable.getY() + (1f - scale) * drawable.getHeight() / 2;

                    canvas.translate(xpos, ypos);
                    canvas.scale(scale, scale);

                    // draw the drawable
                    drawable.draw(canvas);

                    // restore canvas state
                    canvas.restore();
                }
            }
        }
    }

    /**
     *
     * @param ms
     */
    public void goToTime(int ms) {
        for (
            int currentMs = 0;
            currentMs < ms &&
            animationsManager.advance(AnimationsManager.ANIMATION_TIME_INTERVAL);
            currentMs += AnimationsManager.ANIMATION_TIME_INTERVAL
        );

        draw();
    }

    /**
     *
     * @param touchable
     */
    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    /*
     * remember the last hovered drawable
     *
     */
    private Touchable hoveredTouchable = null;

    /**
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /**
         * if view is not touchable
         */
        if (!touchable) {
            return super.onTouchEvent(event);
        }

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                if (hoveredTouchable != null && (!hoveredTouchable.isTouchable() ||
                        !hoveredTouchable.bound((int) event.getX() - hoveredTouchable.getX(), (int) event.getY() - hoveredTouchable.getY()))) {
                    hoveredTouchable.touchOut();
                    hoveredTouchable = null;
                }
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                for (final java.util.ListIterator<Drawable> it = drawables.listIterator(drawables.size()); it.hasPrevious();) {
                    Drawable drawable = it.previous();
                    if (drawable instanceof Touchable) {
                        Touchable touchable = (Touchable) drawable;

                        if (
                            touchable.isTouchable() &&
                            touchable.bound((int) event.getX() - touchable.getX(), (int) event.getY() - touchable.getY())
                        ) {
                            if (action == MotionEvent.ACTION_DOWN) {
                                touchable.touchStart();
                                if (hoveredTouchable == null) {
                                    hoveredTouchable = touchable;
                                    hoveredTouchable.touchOver();
                                }
                            } else if (action == MotionEvent.ACTION_MOVE) {
                                touchable.touchMove();
                                if (hoveredTouchable == null) {
                                    hoveredTouchable = touchable;
                                    hoveredTouchable.touchOver();
                                }
                            } else {
                                touchable.touchUp();
                                if (hoveredTouchable != null) {
                                    hoveredTouchable.touchOut();
                                    hoveredTouchable = null;
                                }
                            }

                            draw();
                            break;
                        }
                    }
                }
                break;
        }

        return true;
    }
}
