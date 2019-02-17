package com.ttyrovou.snake.panels;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ttyrovou.snake.AndroidUtils;
import com.ttyrovou.snake.MainThread;
import com.ttyrovou.snake.sprites.Background;
import com.ttyrovou.snake.sprites.GameOverScreen;
import com.ttyrovou.snake.sprites.PlayerSprite;
import com.ttyrovou.snake.sprites.ScreenText;
import com.ttyrovou.snake.sprites.SnakeBoard;

import main.Game;
import main.GameConfig;
import main.Player;
import timber.log.Timber;

public class SnakePanel extends SurfaceView implements SurfaceHolder.Callback, Player.MoveUpdateListener, PlayerSprite.OnAnimationCompletedListener {

    private Context context;
    private MainThread thread;
    private Background background;
    private SnakeBoard snakeBoard;
    private PlayerSprite[] playerSprites;
    private Game game;
    private ScreenText round, player1Name, player2Name, player1Apples, player2Apples, player1Score, player2Score;
    private GameOverScreen gameOverScreen;
    private boolean gameOver = false;
    private boolean uienabled = true;
    private GameConfig currentGameConfig;

    private static final int TEXT_MARGIN = (int) AndroidUtils.convertDpToPixel(16);

    public SnakePanel(Context context, GameConfig config) throws IllegalArgumentException {
        super(context);

        this.context = context;
        this.currentGameConfig = config;
        try {
            game = new Game(config, this);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid arguements");
        }

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
        round.draw(canvas);
        if (playerSprites.length == 2) {
            player1Name.draw(canvas);
            player1Apples.draw(canvas);
            player1Score.draw(canvas);
            player2Name.draw(canvas);
            player2Apples.draw(canvas);
            player2Score.draw(canvas);
        }
        for (PlayerSprite playerSprite : playerSprites) {
            playerSprite.draw(canvas);
        }
        if (gameOver)
            gameOverScreen.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initGraphics();

        Timber.e("Surface created");
        thread = new MainThread(getHolder(), this);
        thread.start();
        thread.setRunning(true);

    }

    public void initGraphics() {
        background = new Background(getWidth(), getHeight(), Color.rgb(255, 183, 81));
        snakeBoard = new SnakeBoard(context, game.getBoard(), getWidth(), (int) (getHeight() * 0.7));
        playerSprites = new PlayerSprite[game.getGamePlayers().size()];
        for (int i = 0; i < game.getGamePlayers().size(); i++) {
            playerSprites[i] = new PlayerSprite(snakeBoard.getTileById(game.getPlayerPositions().get(i)).getRect(), Color.BLACK, this);
        }

        round = new ScreenText("Round: " + game.getRound(), 0, (int) (getHeight() * 0.8), getWidth(), 18);
        // center text
        round.setX((int) (getWidth() / 2 - round.getMeasuredWidth() / 2));
        if (game.getGamePlayers().size() == 2) {
            player1Name = new ScreenText(game.getGamePlayers().get(0).getName(), TEXT_MARGIN, (int) (getHeight() * 0.7),
            (int) (getWidth() * 0.3), 20);
            player1Apples = new ScreenText("Apple score: " + game.getGamePlayers().get(0).getScore(),
                    TEXT_MARGIN, (int) (getHeight() * 0.7) + player1Name.getHeight(), (int) (getWidth() * 0.35),
                    14);
            player1Score = new ScreenText("Total score: ",
                    TEXT_MARGIN, (int) (getHeight() * 0.7) + player1Name.getHeight() + player1Apples.getHeight(),
                    (int) (getWidth() * 0.35), 14);

            player2Name = new ScreenText(game.getGamePlayers().get(1).getName(), TEXT_MARGIN + (int) (getWidth() * 0.6),
                    (int) (getHeight() * 0.7),
                    (int) (getWidth() * 0.3), 20);
            player2Apples = new ScreenText("Apple score: " + game.getGamePlayers().get(1).getScore(),
                    TEXT_MARGIN + (int) (getWidth() * 0.6), (int) (getHeight() * 0.7) + player2Name.getHeight(),
                    (int) (getWidth() * 0.35), 14);
            player2Score = new ScreenText("Total score: ",
                    TEXT_MARGIN + (int) (getWidth() * 0.6), (int) (getHeight() * 0.7) +
                    player2Name.getHeight() + player2Apples.getHeight(), (int) (getWidth() * 0.35), 14);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && uienabled && !gameOver) {
            game.progressTurn();
        } else if (gameOver) {
            if (gameOverScreen.isRetryClciked(event.getX(), event.getY())) {
                game = new Game(currentGameConfig, this);
                initGraphics();
                gameOver = false;
            } else if (gameOverScreen.isMainMenuClicked(event.getX(), event.getY())) {
                ((Activity) context).finish();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onMoveEvent(Player player, int oldPos, int newPos, Player.MoveEvent moveEvent) {
        int playerIndex = game.findPlayerIndexById(player.getPlayerId());
        PlayerSprite movedPlayerSprite = playerSprites[playerIndex];
        switch (moveEvent) {
            case MOVE_DUE_TO_DIE_THROW: {
                uienabled = false;
                for (int i = oldPos + 1; i < newPos + 1; i++) {
                    Rect destination = snakeBoard.getTileById(i).getRect();
                    movedPlayerSprite.startAnimation(destination);
                }
                break;
            }
            case MOVE_DUE_TO_LADDER: {
                uienabled = false;
                Rect destination = snakeBoard.getTileById(newPos).getRect();
                movedPlayerSprite.startAnimation(destination);
                break;
            }
            case MOVE_DUE_TO_SNAKE: {
                uienabled = false;
                Rect destination = snakeBoard.getTileById(newPos).getRect();
                movedPlayerSprite.startAnimation(destination);
                break;
            }
            case TURN_COMPLETED: {
                // indicates the end of a turn
                movedPlayerSprite.startAnimation(null);
            }
        }
    }

    @Override
    public void onAppleConsumption(int points) {

    }

    @Override
    public void onSmallAnimationCompleted() {

    }

    @Override
    public void onIntermediateAnimationCompleted() {

    }

    @Override
    public void onFullAnimationCompleted() {
        uienabled = true;
        snakeBoard.updateBoard(game.getBoard());
        round.setText("Round: " + game.getRound());
        if (game.getGamePlayers().size() == 2) {
            player1Apples.setText("Apple score: " + game.getGamePlayers().get(0).getScore());
            player2Apples.setText("Apple score: " + game.getGamePlayers().get(1).getScore());
        }
        if (game.isGameOver()) {
            int maxScoreIndex = 0;
            for (int i = 0; i < game.getGamePlayers().size(); i++) {
                if (game.getGamePlayers().get(i).getScore() > game.getGamePlayers().get(maxScoreIndex).getScore())
                    maxScoreIndex = i;
            }

            gameOverScreen = new GameOverScreen(context, (int) (getWidth() * 0.1), (int) (getHeight() * 0.3),
                    (int) (getWidth() * 0.8), (int) (getHeight() * 0.4), game.getGamePlayers().get(maxScoreIndex).getName());
            gameOver = true;
        }
    }
}
