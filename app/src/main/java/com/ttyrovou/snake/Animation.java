package com.ttyrovou.snake;

import android.graphics.Rect;

import com.ttyrovou.snake.sprites.PlayerSprite;

import main.PlayerState;

/**
 * Contains information about animations that {@link PlayerSprite} can execute
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
public class Animation {

    // the small animations when walking due to dice throw
    public static final int WALK = 0;
    // ladder or snake
    public static final int LADDER_OR_SNAKE = 1;

    private Rect destination;
    private int type, duration;
    /*
    If not null, indicates that the board should be updated after this animation
     */
    private PlayerState playerState;

    public Animation(Rect destination, int type, PlayerState playerState) {
        this.destination = destination;
        this.type = type;
        this.playerState = playerState;
        this.duration = PlayerSprite.ANIMATION_DURATION;
    }

    public Animation(Rect destination, int type, PlayerState playerState, int duration) {
        this.destination = destination;
        this.type = type;
        this.playerState = playerState;
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public Rect getDestination() {
        return destination;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public int getDuration() {
        return duration;
    }
}
