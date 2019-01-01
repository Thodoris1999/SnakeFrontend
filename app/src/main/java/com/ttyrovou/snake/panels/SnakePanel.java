package com.ttyrovou.snake.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.app.WindowDecorActionBar;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ttyrovou.snake.GameOverScreen;
import com.ttyrovou.snake.MainThread;
import com.ttyrovou.snake.sprites.Background;
import com.ttyrovou.snake.sprites.PlayerSprite;
import com.ttyrovou.snake.sprites.SnakeBoard;

import java.util.LinkedList;

import main.Game;
import main.Player;
import timber.log.Timber;

public class SnakePanel extends SurfaceView implements SurfaceHolder.Callback, Player.MoveUpdateListener, PlayerSprite.OnAnimationCompletedListener {

    private Context context;
    private MainThread thread;
    private Background background;
    private SnakeBoard snakeBoard;
    private PlayerSprite[] playerSprites;
    private Game game;
    private GameOverScreen gameOverScreen;
    private boolean gameOver = false;

    private boolean uienabled = true;

    public SnakePanel(Context context) {
        super(context);

        this.context = context;
        game = new Game(2, 50, this);;

        thread = new MainThread(getHolder(), this);

        getHolder().addCallback(this);
        setFocusable(true);
        setWillNotDraw(false);
    }

    public void update() {
        for (PlayerSprite playerSprite : playerSprites) {
            playerSprite.update();
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        background.draw(canvas);
        snakeBoard.draw(canvas);
        for (PlayerSprite playerSprite : playerSprites) {
            playerSprite.draw(canvas);
        }
        if (gameOver)
            gameOverScreen.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initGraphics();

        thread.setRunning(true);
        thread.start();
    }

    public void initGraphics() {
        background = new Background(getWidth(), getHeight(), Color.rgb(255, 183, 81));
        snakeBoard = new SnakeBoard(context, game.getBoard(), getWidth(), (int) (getHeight() * 0.7));
        playerSprites = new PlayerSprite[game.getGamePlayers().size()];
        for (int i = 0; i < game.getGamePlayers().size(); i++) {
            playerSprites[i] = new PlayerSprite(snakeBoard.getTileById(game.getPlayerPositions().get(i)).getRect(), Color.BLACK, this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && uienabled) {
            Toast.makeText(getContext(), "x: " + event.getX() + ", y: " + event.getY(), Toast.LENGTH_SHORT).show();
            game.progressTurn();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onMoveEvent(Player player, int oldPos, int newPos, Player.MoveEvent moveEvent) {
        int playerIndex = game.findPlayerIndexById(player.getPlayerId());
        PlayerSprite movedPlayerSprite = playerSprites[playerIndex];
        Timber.d("move info " + oldPos + " " + newPos + " " + moveEvent);
        switch (moveEvent) {
            case MOVE_DUE_TO_DIE_THROW: {
                uienabled = false;
                for (int i = oldPos + 1; i < newPos + 1; i++) {
                    Rect destination = snakeBoard.getTileById(i).getRect();
                    movedPlayerSprite.startAnimation(destination);
                }
                break;
            } case MOVE_DUE_TO_LADDER: {
                uienabled = false;
                Rect destination = snakeBoard.getTileById(newPos).getRect();
                movedPlayerSprite.startAnimation(destination);
                break;
            } case MOVE_DUE_TO_SNAKE: {
                uienabled = false;
                Rect destination = snakeBoard.getTileById(newPos).getRect();
                movedPlayerSprite.startAnimation(destination);
                break;
            } case TURN_COMPLETED: {
                // indicates the end of a turn
                movedPlayerSprite.startAnimation(null);
            }
        }
    }

    @Override
    public void onAppleConsumption(int points) {

    }

    @Override
    public void onAnimationCompleted() {
        uienabled = true;
        snakeBoard.updateBoard(game.getBoard());
        checkForGameEnd();
    }

    private void checkForGameEnd() {
        if (game.getMaxRounds() == game.getRound()) {
            int maxScoreIndex = 0;
            for (int i = 0; i < game.getGamePlayers().size(); i++) {
                if (game.getGamePlayers().get(i).getScore() > game.getGamePlayers().get(maxScoreIndex).getScore())
                    maxScoreIndex = i;
            }

            gameOverScreen = new GameOverScreen((int) (getWidth() * 0.1), (int) (getHeight() * 0.3),
                    (int) (getWidth() * 0.8), (int) (getHeight() * 0.4), game.getGamePlayers().get(maxScoreIndex).getName());
            gameOver = true;
        }
    }
}
