package com.ttyrovou.snake.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ttyrovou.snake.AndroidUtils;
import com.ttyrovou.snake.Animation;

import java.util.LinkedList;

import main.PlayerState;

/**
 * Draws and animates the players sprites (circles)
 *
 * @author Τυροβούζης Θεόδωρος
 * AEM 9369
 * phone number 6955253435
 * email ttyrovou@ece.auth.gr
 *
 * @author Τσιμρόγλου Στυλιανός
 * AEM 9468
 * phone number 6977030504
 * email stsimrog@ece.auth.gr
 */
public class PlayerSprite extends BaseSprite implements Dice.OnDiceFinishedListener {

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
        radius = (float) Math.min(radius * 0.8, AndroidUtils.convertDpToPixel(32));
        this.x = (rect.right - rect.left) / 2;
        this.y = rect.top + (rect.bottom - rect.top) / 2;
        this.p = new Paint();
        p.setColor(color);
        this.animationCompletedListener = completedListener;
    }

    /**
     * Calculates the x and y coordinates of the progress of the current animation
     */
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

    public void setColor(int a, int r, int g, int b) {
        p.setARGB(a, r, g, b);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, p);
    }

    // http://cogitolearning.co.uk/2013/10/android-animations-tutorial-5-more-on-interpolators/
    private float accelerateDecelerateInterpolator(float time) {
        return (float) (Math.cos((time + 1) * Math.PI) / 2 + 0.5);
    }

    /**
     * Consumes the first animation from the priority queue {@link PlayerSprite#animationQueue}
     * @param xend the x position of the destination at the end of the animation
     * @param yend the y position of the destination at the end of the animation
     * @param duration the animation duration in frames
     */
    private void consumeAnimation(float xend, float yend, int duration) {
        animationStartX = x;
        animationStartY = y;
        animationEndX = xend;
        animationEndY = yend;
        animationRemainingFrames = duration;
    }

    /**
     * Consumes the first animation from the priority queue {@link PlayerSprite#animationQueue}
     */
    public void consumeAnimation() {
        Animation animation = animationQueue.getFirst();
        consumeAnimation(animation.getDestination().left +
                        (animation.getDestination().right - animation.getDestination().left) / 2,
                animation.getDestination().top +
                        (animation.getDestination().bottom - animation.getDestination().top) / 2,
                animation.getDuration());
    }

    public void addAnimation(Animation animation) {
        animationQueue.add(animation);
    }

    /**
     * Gets called when any animation finish is detected. Gives the appropriate callback to {@link com.ttyrovou.snake.panels.SnakePanel}
     * based on the type of animation that was finished. Then tries to move on to the next animation,
     * if it exists. Otherwise it dispatches the event that all animations are finished.
     */
    public void onSmallAnimationFished() {
        if (animationQueue.getFirst().getType() == Animation.LADDER_OR_SNAKE ||
                (animationQueue.size() > 1 && animationQueue.getFirst().getType() == Animation.WALK && animationQueue.get(1).getType() != Animation.WALK)) {
            animationCompletedListener.onIntermediateAnimationCompleted(animationQueue.getFirst().getPlayerState());
        }
        if (animationQueue.size() > 1 && animationQueue.getFirst().getType() == Animation.WALK &&
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

    /**
     * Gets called when the dice finishes animating. Commands player animations to start
     */
    @Override
    public void onDiceFinished() {
        consumeAnimation();
    }

    /**
     * Callback that dispatches the end of a certain type of animation
     */
    public interface OnAnimationCompletedListener {
        void onWalkAnimationCompleted(PlayerState playerState);
        void onIntermediateAnimationCompleted(PlayerState playerState);
        void onFullAnimationCompleted();
    }

    public LinkedList<Animation> getAnimationQueue() {
        return animationQueue;
    }
}
