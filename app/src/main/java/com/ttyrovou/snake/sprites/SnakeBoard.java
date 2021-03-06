package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import main.Board;
import main.PlayerState;

/**
 * Draws the board of the game and everything it contains
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
public class SnakeBoard extends BaseSprite {

    private final int[] tileColors = {Color.RED, Color.WHITE, Color.GREEN, Color.YELLOW, Color.CYAN};

    private Tile[][] tileBoard;
    private Apple[] apples;
    private Ladder[] ladders;
    private Snake[] snakes;

    public SnakeBoard(int rows, int columns, int width, int height, int marginLeft, int marginTop) {
        tileBoard = new Tile[rows][columns];
        int tileWidth = width / columns;
        int tileHeight = height / rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int id = columns * rows - columns - columns * i + (((rows - 1 - i) % 2 == 0) ? (j + 1) : (columns - j));
                // alternate between a set of colors
                int colorIndex = (i - j + columns - 1) % tileColors.length;
                tileBoard[i][j] = new Tile(marginLeft + j * tileWidth, marginTop + i * tileHeight,
                        tileWidth, tileHeight, tileColors[colorIndex], id);
            }
        }
    }

    public SnakeBoard(Context context, Board board, int width, int height) {
        tileBoard = new Tile[board.getM()][board.getN()];
        int tileWidth = width / board.getN();
        int tileHeight = height / board.getM();
        for (int i = 0; i < board.getM(); i++) {
            for (int j = 0; j < board.getN(); j++) {
                int id = board.getN() * board.getM() - board.getN() - board.getN() * i + (((board.getM() - 1 - i) % 2 == 0) ? (j + 1) : (board.getN() - j));
                // alternate between a set of colors
                int colorIndex = (i - j + board.getN() - 1) % tileColors.length;
                tileBoard[i][j] = new Tile(j * tileWidth, i * tileHeight,
                        tileWidth, tileHeight, tileColors[colorIndex], id);
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

        snakes = new Snake[board.getSnakes().length];
        for (int i = 0; i < board.getSnakes().length; i++) {
            main.Snake snake = board.getSnakes()[i];
            Rect tail = getTileById(snake.getTailId()).getRect();
            Rect head = getTileById(snake.getHeadId()).getRect();
            snakes[i] = new Snake(context, tail, head);
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
        for (Snake snake : snakes) {
            snake.draw(canvas);
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

    /**
     * Utility method to get tile object by their tileId instead of giving array arguments
     * @param id the id requested
     * @return the tile corresponding to that id
     */
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

    /**
     * Draws a single tile in a board along with its number on the top left
     */
    public static class Tile extends BaseSprite {

        private Rect rect;
        private Paint paint;
        private ScreenText tileId;

        public Tile(int x, int y, int width, int height, int color, int id) {
            rect = new Rect(x, y, x + width, y + height);
            paint = new Paint();
            paint.setColor(color);

            tileId = new ScreenText(Integer.toString(id), x, y, width, 8, 255, 0, 0, 0);
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
            tileId.draw(canvas);
        }
    }
}
