package com.ttyrovou.snake;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.ttyrovou.snake.sprites.BaseSprite;

public class GameOverScreen extends BaseSprite {

    private Rect rect;
    private Paint windowPaint;
    private Paint textPaint;
    private String winnerName;

    private float TEXT_MARGIN_LEFT = 10;
    private float TEXT_MARGIN_TOP = 10;

    public GameOverScreen(int x, int y, int width, int height, String winnerName) {
        rect = new Rect(x, y, x + width, y + height);
        windowPaint = new Paint();
        windowPaint.setColor(Color.LTGRAY);
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        this.winnerName = winnerName;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, windowPaint);
        canvas.drawText(winnerName + " has won!", rect.top + TEXT_MARGIN_LEFT, rect.left + TEXT_MARGIN_TOP, textPaint);
    }
}
