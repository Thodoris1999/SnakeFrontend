package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.WindowDecorActionBar;

import main.Board;
import main.Game;
import timber.log.Timber;

public class SnakeBoard extends BaseSprite {

    private final int[] tileColors = {Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW, Color.CYAN};

    private Tile[][] tileBoard;
    private Apple[] apples;
    private Ladder[] ladders;

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

    public SnakeBoard(Context context, Board board, int width, int height) {
        tileBoard = new Tile[board.getM()][board.getN()];
        int tileWidth = width / board.getN();
        int tileHeight = height / board.getM();
        for (int i = 0; i < board.getM(); i++) {
            for (int j = 0; j < board.getN(); j++) {
                // alternate between a set of colors
                int colorIndex = (i - j + board.getN() - 1) % tileColors.length;
                tileBoard[i][j] = new Tile(j * tileWidth, i * tileHeight,
                        tileWidth, tileHeight, tileColors[colorIndex]);
            }
        }

        apples = new Apple[board.getApples().length];
        for (int i = 0; i < board.getApples().length; i++) {
            main.Apple apple = board.getApples()[i];
            apples[i] = new Apple(context, getTileById(apple.getAppleTileId()).getRect(), apple.getPoints());
        }

        ladders = new Ladder[board.getLadders().length];
        for (int i = 0; i < board.getLadders().length; i++) {
            main.Ladder ladder = board.getLadders()[i];
            Rect lowStep = getTileById(ladder.getDownstepId()).getRect();
            Rect highStep = getTileById(ladder.getUpstepId()).getRect();
            ladders[i] = new Ladder(context, lowStep, highStep);
        }
    }

    public Tile getTile(int x, int y) {
        return tileBoard[x][y];
    }

    @Override
    public void draw(Canvas canvas) {
        for (Tile[] row : tileBoard) {
            for (Tile tile : row) {
                tile.draw(canvas);
            }
        }
        for (Apple apple : apples) {
            apple.draw(canvas);
        }
        for (Ladder ladder : ladders) {
            ladder.draw(canvas);
        }
    }

    public void updateBoard(Board board) {
        for (int i = 0; i < board.getApples().length; i++) {
            main.Apple apple = board.getApples()[i];
            apples[i].update(apple.getPoints());
        }

        for (int i = 0; i < board.getLadders().length; i++) {
            main.Ladder ladder = board.getLadders()[i];
            ladders[i].update(ladder.isBroken());
        }
    }

    public Tile getTileById(int id) {
        if (id == 0) {
            //starting tile
            Rect bottomLeftTileRect = new Rect(tileBoard[tileBoard.length - 1][0].getRect());
            bottomLeftTileRect.offset(0, bottomLeftTileRect.bottom - bottomLeftTileRect.top);
            return new Tile(bottomLeftTileRect, tileBoard[tileBoard.length - 1][0].paint.getColor());
        }
        id -= 1;
        int row = id / tileBoard[0].length;
        int column = (row % 2 == 0) ? (id - tileBoard[0].length * row) : (tileBoard[0].length - 1 - id + tileBoard[0].length * row);
        return tileBoard[tileBoard.length - 1 - row][column];
    }

    public static class Tile extends BaseSprite {

        private Rect rect;
        private Paint paint;

        public Tile(int x, int y, int width, int height, int color) {
            rect = new Rect(x, y, x + width, y + height);
            paint = new Paint();
            paint.setColor(color);
        }

        public Tile(Rect rect, int color) {
            this.rect = rect;
            paint = new Paint();
            paint.setColor(color);
        }

        public Rect getRect() {
            return rect;
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(rect, paint);
        }
    }
}
