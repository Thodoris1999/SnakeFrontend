package com.ttyrovou.snake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ttyrovou.snake.panels.SnakePanel;

import main.GameConfig;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent callingIntent = getIntent();

        GameConfig gameConfig = new GameConfig();
        gameConfig.setNumRows(callingIntent.getIntExtra(MainMenuActivity.NUM_ROWS_KEY, 10));
        gameConfig.setNumCols(callingIntent.getIntExtra(MainMenuActivity.NUM_COLS_KEY, 10));
        gameConfig.setNumPlayers(callingIntent.getIntExtra(MainMenuActivity.NUM_PLAYERS_KEY, 2));
        gameConfig.setNumLadders(callingIntent.getIntExtra(MainMenuActivity.NUM_LADDERS_KEY, 3));
        gameConfig.setNumSnakes(callingIntent.getIntExtra(MainMenuActivity.NUM_SNAKES_KEY, 3));
        gameConfig.setNumApples(callingIntent.getIntExtra(MainMenuActivity.NUM_APPLES_KEY, 6));

        setContentView(new SnakePanel(this, gameConfig));
    }
}
