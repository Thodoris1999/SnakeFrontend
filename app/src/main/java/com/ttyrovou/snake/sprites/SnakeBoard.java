package com.ttyrovou.snake.sprites;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class SnakeBoard extends BaseSprite {

    private final int[] tileColors = {Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW, Color.CYAN};

    private Tile[][] tileBoard;

    public SnakeBoard(int rows, int columns, int width, int height, int marginLeft, int marginTop) {
        tileBoard = new Tile[rows][columns];
        int tileWidth = width / columns;
        int tileHeight = height / rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // alternate between a set of colors
                int colorIndex = (i - j + columns - 1) % tileColors.length;
                tileBoard[i][j] = new Tile(marginLeft + j * tileWidth, marginTop + i * tileHeight,
                        tileWidth, tileHeight, tileColors[colorIndex]);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        for (Tile[] row : tileBoard) {
            for (Tile tile : row) {
                tile.draw(canvas);
            }
        }
    }

    static class Tile extends BaseSprite {

        private Rect rect;
        private Paint paint;

        public Tile(int x, int y, int width, int height, int color) {
            rect = new Rect(x, y, x + width, y + height);
            paint = new Paint();
            paint.setColor(color);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(rect, paint);
        }
    }
}
