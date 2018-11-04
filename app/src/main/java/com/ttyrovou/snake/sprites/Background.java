package com.ttyrovou.snake.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Background extends BaseSprite {

    private Rect rect;
    private Paint paint;

    public Background(int width, int height, int color) {
        rect = new Rect(0, 0, width, height);
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }
}
