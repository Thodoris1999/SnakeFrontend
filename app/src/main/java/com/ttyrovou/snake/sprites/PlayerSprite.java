package com.ttyrovou.snake.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.LinkedList;

public class PlayerSprite extends BaseSprite {

    public static final int ANIMATION_DURATION = 10; // in frames

    private float x, y, radius;
    private Paint p;

    private int animationRemainingFrames = 0;
    private float animationStartX, animationStartY, animationEndX, animationEndY;
    private LinkedList<Rect> animationQueue = new LinkedList<>();

    private OnAnimationCompletedListener animationCompletedListener;

    public PlayerSprite(float x, float y, float radius, int color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.p = new Paint();
        p.setColor(color);
    }

    public PlayerSprite(Rect rect, int color, OnAnimationCompletedListener completedListener) {
        this.radius = (rect.right - rect.left) / 2;
        this.x = rect.left + radius;
        this.y = rect.top + (rect.bottom - rect.top) / 2;
        radius *= 0.8;
        this.p = new Paint();
        p.setColor(color);
        this.animationCompletedListener = completedListener;
    }

    public void update() {
        if (animationRemainingFrames > 0) {
            animationRemainingFrames--;
            float animationTime = 1 - (float) animationRemainingFrames / (float) ANIMATION_DURATION;
            float animationProgress = accelerateDecelerateInterpolator(animationTime);
            x = animationProgress * (animationEndX - animationStartX) + animationStartX;
            y = animationProgress * (animationEndY - animationStartY) + animationStartY;
            if (animationRemainingFrames == 1) {
                onSmallAnimationFished();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, p);
    }

    // http://cogitolearning.co.uk/2013/10/android-animations-tutorial-5-more-on-interpolators/
    public float accelerateDecelerateInterpolator(float time) {
        return (float) (Math.cos((time + 1) * Math.PI) / 2 + 0.5);
    }

    public void startAnimation(float xend, float yend) {
        animationStartX = x;
        animationStartY = y;
        animationEndX = xend;
        animationEndY = yend;
        animationRemainingFrames = ANIMATION_DURATION;
    }

    public void startAnimation(Rect destination) {
        animationQueue.add(destination);
        if (animationRemainingFrames == 0) {
            startAnimation(destination.left + radius, destination.top + (destination.bottom - destination.top) / 2);
        }
    }

    public void onSmallAnimationFished() {
        animationQueue.removeFirst();
        if (!animationQueue.isEmpty()) {
            Rect destination = animationQueue.getFirst();
            if (destination == null) {
                animationQueue.removeFirst();
                animationCompletedListener.onAnimationCompleted();
            } else {
                startAnimation(destination.left + radius, destination.top + (destination.bottom - destination.top) / 2);
            }
        }
    }

    public interface OnAnimationCompletedListener {
        void onAnimationCompleted();
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }
}
