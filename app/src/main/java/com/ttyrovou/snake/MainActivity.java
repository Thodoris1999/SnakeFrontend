package com.ttyrovou.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ttyrovou.snake.panels.SnakePanel;

import main.GameConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent callingIntent = getIntent();

        GameConfig gameConfig = (GameConfig) callingIntent.getSerializableExtra(MainMenuActivity.GAME_CONFIG_KEY);

        try {
            setContentView(new SnakePanel(this, gameConfig));
        } catch (IllegalArgumentException iae) {
            Toast.makeText(this, "Could not created game with given values", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
