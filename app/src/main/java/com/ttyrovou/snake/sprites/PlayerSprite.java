package com.ttyrovou.snake.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ttyrovou.snake.Animation;

import java.util.LinkedList;

import main.PlayerState;
import timber.log.Timber;

public class PlayerSprite extends BaseSprite {

    public static final int ANIMATION_DURATION = 10; // in frames

    private float x, y, radius;
    private Paint p;

    private int animationRemainingFrames = 0;
    private float animationStartX, animationStartY, animationEndX, animationEndY;
    private LinkedList<Animation> animationQueue = new LinkedList<>();

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
        radius *= 0.8;
        this.x = (rect.right - rect.left) / 2;
        this.y = rect.top + (rect.bottom - rect.top) / 2;
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
    private float accelerateDecelerateInterpolator(float time) {
        return (float) (Math.cos((time + 1) * Math.PI) / 2 + 0.5);
    }

    private void consumeAnimation(float xend, float yend, int duration) {
        animationStartX = x;
        animationStartY = y;
        animationEndX = xend;
        animationEndY = yend;
        animationRemainingFrames = duration;
    }

    private void consumeAnimation() {
        Animation animation = animationQueue.getFirst();
        consumeAnimation(animation.getDestination().left +
                        (animation.getDestination().right - animation.getDestination().left) / 2,
                animation.getDestination().top +
                        (animation.getDestination().bottom - animation.getDestination().top) / 2,
                animation.getDuration());
    }

    public void addAnimation(Animation animation) {
        animationQueue.add(animation);
        if (animationRemainingFrames == 0) {
            consumeAnimation();
        }
    }

    public void onSmallAnimationFished() {
        if (animationQueue.getFirst().getType() == Animation.LADDER_OR_SNAKE ||
                (animationQueue.size() > 1 && animationQueue.getFirst().getType() == Animation.WALK && animationQueue.get(1).getType() != Animation.WALK)) {
            animationCompletedListener.onIntermediateAnimationCompleted(animationQueue.getFirst().getPlayerState());
        } else if (animationQueue.size() > 1 && animationQueue.getFirst().getType() == Animation.WALK &&
                animationQueue.get(1).getType() == Animation.WALK) {
            animationCompletedListener.onWalkAnimationCompleted(animationQueue.getFirst().getPlayerState());
        }
        animationQueue.removeFirst();
        if (animationQueue.isEmpty())
            animationCompletedListener.onFullAnimationCompleted();
        else {
            consumeAnimation();
        }
    }

    public interface OnAnimationCompletedListener {
        void onWalkAnimationCompleted(PlayerState playerState);
        void onIntermediateAnimationCompleted(PlayerState playerState);
        void onFullAnimationCompleted();
    }

    public LinkedList<Animation> getAnimationQueue() {
        return animationQueue;
    }
}
