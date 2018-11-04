package com.ttyrovou.snake.sprites;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SnakeBoard extends BaseSprite {

    private Tile[][] tileBoard;

    public SnakeBoard(int rows, int columns) {
        tileBoard = new Tile[rows][columns];
    }

    @Override
    public void draw(Canvas canvas) {

    }

    static class Tile extends BaseSprite {

        private Rect rect;
        private Paint paint;

        public Tile(int x, int y, int size, int color) {
            rect = new Rect(x, y, x + size, y + size);
            paint = new Paint();
            paint.setColor(color);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(rect, paint);
        }
    }
}
