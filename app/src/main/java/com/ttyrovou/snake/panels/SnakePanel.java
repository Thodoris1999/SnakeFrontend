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
import com.ttyrovou.snake.Animation;
import com.ttyrovou.snake.MainThread;
import com.ttyrovou.snake.sprites.Background;
import com.ttyrovou.snake.sprites.Dice;
import com.ttyrovou.snake.sprites.GameOverScreen;
import com.ttyrovou.snake.sprites.PlayerSprite;
import com.ttyrovou.snake.sprites.ScreenText;
import com.ttyrovou.snake.sprites.SnakeBoard;

import java.text.DecimalFormat;

import main.Game;
import main.GameConfig;
import main.Player;
import main.PlayerState;
import timber.log.Timber;

public class SnakePanel extends SurfaceView implements SurfaceHolder.Callback, Player.MoveUpdateListener,
        PlayerSprite.OnAnimationCompletedListener {

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
    private Dice dice;

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
        dice.update();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //clears canvas
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
        dice.draw(canvas);
        if (gameOver)
            gameOverScreen.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initGraphics();

        thread = new MainThread(getHolder(), this);
        thread.start();
        thread.setRunning(true);

    }

    public void initGraphics() {
        background = new Background(getWidth(), getHeight(), Color.rgb(255, 183, 81));
        if (game.getBoard().getM() == 1) {
            snakeBoard = new SnakeBoard(context, game.getBoard(), getWidth(), (int) (getHeight() * 0.25));
        } else if (game.getBoard().getM() == 2) {
            snakeBoard = new SnakeBoard(context, game.getBoard(), getWidth(), (int) (getHeight() * 0.5));
        } else {
            snakeBoard = new SnakeBoard(context, game.getBoard(), getWidth(), (int) (getHeight() * 0.7));
        }
        playerSprites = new PlayerSprite[game.getGamePlayers().size()];
        for (int i = 0; i < game.getGamePlayers().size(); i++) {
            playerSprites[i] = new PlayerSprite(snakeBoard.getTileById(game.getPlayerPositions().get(i)).getRect(), Color.BLACK, this);
        }
        dice = new Dice(context, (int) (getWidth() * 0.35), (int) (getHeight() - 0.3 * getHeight() - TEXT_MARGIN),
                (int) (getWidth() * 0.3));

        round = new ScreenText("Round: " + game.getRound(), 0, (int) (getHeight() * 0.8), getWidth(),
                18, 255, 0, 0, 0);
        // center text
        round.setX((int) (getWidth() / 2 - round.getMeasuredWidth() / 2));
        if (game.getGamePlayers().size() == 2) {
            // align player info to the bottom of the screen
            player1Score = new ScreenText("Total score: " +
                    Player.evaluate(game.getPlayerPositions().get(0), game.getGamePlayers().get(0).getScore()),
                    TEXT_MARGIN, 0,
                    (int) (getWidth() * 0.35), 14, 255, 0, 0, 128);
            player1Score.setY(getHeight() - player1Score.getHeight() - TEXT_MARGIN);
            player1Apples = new ScreenText("Apple score: " + game.getGamePlayers().get(0).getScore(),
                    TEXT_MARGIN, 0, (int) (getWidth() * 0.35),
                    14, 255, 0, 0, 128);
            player1Apples.setY(getHeight() - player1Score.getHeight() - player1Apples.getHeight() - TEXT_MARGIN);
            player1Name = new ScreenText(game.getGamePlayers().get(0).getName(), TEXT_MARGIN, 0,
                    (int) (getWidth() * 0.3), 20, 255, 0, 0, 128);
            player1Name.setY(getHeight() - player1Score.getHeight() - player1Apples.getHeight()
                    - player1Name.getHeight() - TEXT_MARGIN);

            player2Score = new ScreenText("Total score: " +
                    Player.evaluate(game.getPlayerPositions().get(0), game.getGamePlayers().get(0).getScore()),
                    TEXT_MARGIN + (int) (getWidth() * 0.6), 0, (int) (getWidth() * 0.35), 14,
                    255, 128, 0, 0);
            player2Score.setY(getHeight() - player2Score.getHeight() - TEXT_MARGIN);
            player2Apples = new ScreenText("Apple score: " + game.getGamePlayers().get(1).getScore(),
                    TEXT_MARGIN + (int) (getWidth() * 0.6), 0,
                    (int) (getWidth() * 0.35), 14, 255, 128, 0, 0);
            player2Apples.setY(getHeight() - player2Score.getHeight() - player2Apples.getHeight() - TEXT_MARGIN);
            player2Name = new ScreenText(game.getGamePlayers().get(1).getName(), TEXT_MARGIN + (int) (getWidth() * 0.6),
                    0, (int) (getWidth() * 0.3), 20, 255, 128, 0, 0);
            player2Name.setY(getHeight() - player2Score.getHeight() - player2Apples.getHeight()
                    - player2Name.getHeight() - TEXT_MARGIN);

            playerSprites[0].setColor(255, 0, 0, 128);
            playerSprites[1].setColor(255, 128, 0, 0);
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
            Timber.d("Rolling die");
            game.progressTurn();
        } else if (gameOver) {
            if (gameOverScreen.isRetryClciked(event.getX(), event.getY())) {
                game = new Game(currentGameConfig, this);
                initGraphics();
                gameOver = false;
            } else if (gameOverScreen.isMainMenuClicked(event.getX(), event.getY())) {
                Timber.d("Finishing");
                ((Activity) context).finish();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onMoveEvent(PlayerState playerState, int oldPos, Player.MoveEvent moveEvent) {
        int playerIndex = game.findPlayerIndexById(playerState.getPlayerId());
        PlayerSprite movedPlayerSprite = playerSprites[playerIndex];
        switch (moveEvent) {
            case MOVE_DUE_TO_DIE_THROW: {
                uienabled = false;
                Rect destination = snakeBoard.getTileById(playerState.getPosition()).getRect();
                movedPlayerSprite.addAnimation(new Animation(destination, Animation.WALK, playerState));
                break;
            }
            case MOVE_DUE_TO_LADDER: {
                uienabled = false;
                Rect destination = snakeBoard.getTileById(playerState.getPosition()).getRect();
                movedPlayerSprite.addAnimation(new Animation(destination, Animation.LADDER_OR_SNAKE, playerState));
                break;
            }
            case MOVE_DUE_TO_SNAKE: {
                uienabled = false;
                Rect destination = snakeBoard.getTileById(playerState.getPosition()).getRect();
                movedPlayerSprite.addAnimation(new Animation(destination, Animation.LADDER_OR_SNAKE, playerState));
                break;
            }
            case TURN_COMPLETED: {
                // indicates the end of a turn
                // die is the new from the last walk animation minus the oldPos from the first one
                int firstAnimationIndex = movedPlayerSprite.getAnimationQueue().size() - 1;
                for (int i = 0; i < movedPlayerSprite.getAnimationQueue().size(); i++) {
                    if (movedPlayerSprite.getAnimationQueue().get(i).getType() != Animation.WALK) {
                        firstAnimationIndex = i - 1;
                        break;
                    }
                }
                int rolled = movedPlayerSprite.getAnimationQueue().get(firstAnimationIndex).getPlayerState().getPosition() -
                        movedPlayerSprite.getAnimationQueue().getFirst().getPlayerState().getPosition();
                dice.startRolling(rolled, movedPlayerSprite);
                break;
            }
        }
    }

    @Override
    public void onAppleConsumption(PlayerState playerState) {
        int playerIndex = game.findPlayerIndexById(playerState.getPlayerId());
        PlayerSprite movedPlayerSprite = playerSprites[playerIndex];
        movedPlayerSprite.getAnimationQueue().getLast().setPlayerState(playerState);
    }


    @Override
    public void onWalkAnimationCompleted(PlayerState playerState) {
        snakeBoard.updateBoard(playerState.getBoard());
        if (game.getGamePlayers().size() == 2) {
            DecimalFormat formatter = new DecimalFormat("#.#");
            int playerIndex = game.findPlayerIndexById(playerState.getPlayerId());
            if (playerIndex == 0) {
                player1Apples.setText("Apple score: " + playerState.getPoints());
                player1Score.setText("Total score: " + formatter.format(Player.evaluate(playerState.getPosition(),
                        playerState.getPoints())));
            } else {
                player2Apples.setText("Apple score: " + playerState.getPoints());
                player2Score.setText("Total score: " + formatter.format(Player.evaluate(playerState.getPosition(),
                        playerState.getPoints())));
            }
        }
    }

    @Override
    public void onIntermediateAnimationCompleted(PlayerState playerState) {
        Timber.d("Board updated");
        snakeBoard.updateBoard(playerState.getBoard());
        if (game.getGamePlayers().size() == 2) {
            DecimalFormat formatter = new DecimalFormat("#.#");
            int playerIndex = game.findPlayerIndexById(playerState.getPlayerId());
            if (playerIndex == 0) {
                player1Apples.setText("Apple score: " + playerState.getPoints());
                player1Score.setText("Total score: " + formatter.format(Player.evaluate(playerState.getPosition(),
                        playerState.getPoints())));
            } else {
                player2Apples.setText("Apple score: " + playerState.getPoints());
                player2Score.setText("Total score: " + formatter.format(Player.evaluate(playerState.getPosition(),
                        playerState.getPoints())));
            }
        }
    }

    @Override
    public void onFullAnimationCompleted() {
        uienabled = true;
        round.setText("Round: " + game.getRound());
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
