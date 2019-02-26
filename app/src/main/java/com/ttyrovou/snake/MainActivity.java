package com.ttyrovou.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ttyrovou.snake.panels.SnakePanel;

import main.GameConfig;

/**
 * The activity where the user plays the game
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
public class MainActivity extends AppCompatActivity {

    /**
     * Gets called when the activity is created
     */
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
