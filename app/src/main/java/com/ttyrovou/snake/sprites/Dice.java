package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ttyrovou.snake.R;

import java.util.ArrayList;

import timber.log.Timber;

public class Dice extends BaseSprite {

    private static final int MIN_FRAMES_ROLLING = 3 * 6;
    private int[] frames = {R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4, R.drawable.d5, R.drawable.d6};

    private ArrayList<Bitmap> bitmaps = new ArrayList<>(6);
    private Rect dicePosition;
    private int cycle;
    private boolean rolling = false;
    private int numberToStopAt;
    private OnDiceFinishedListener onDiceFinishedListener;

    public Dice(Context context, int x, int y, int size) {
        for (int frame : frames) {
            Bitmap sourceDice = BitmapFactory.decodeResource(context.getResources(), frame);
            Bitmap scaledSnake = Bitmap.createScaledBitmap(sourceDice, size, size, true);
            bitmaps.add(scaledSnake);
        }
        dicePosition = new Rect(x, y, x + bitmaps.get(0).getWidth(), y + bitmaps.get(0).getHeight());
    }

    public void update() {
        if (rolling) {
            cycle++;
            Timber.d("stop number " + numberToStopAt + MIN_FRAMES_ROLLING);
            if (cycle >= MIN_FRAMES_ROLLING + numberToStopAt) {
                //stop
                rolling = false;
                onDiceFinishedListener.onDiceFinished();
            }
        }
    }

    public void startRolling(int numberToStop, OnDiceFinishedListener onDiceFinishedListener) {
        this.numberToStopAt = numberToStop;
        this.onDiceFinishedListener = onDiceFinishedListener;
        cycle = 0;
        rolling = true;
    }

    public interface OnDiceFinishedListener {
        void onDiceFinished();
    }

    @Override
    public void draw(Canvas canvas) {
        Timber.d(Integer.toString(cycle));
        canvas.drawBitmap(bitmaps.get(cycle % 6), null, dicePosition, null);
    }
}
