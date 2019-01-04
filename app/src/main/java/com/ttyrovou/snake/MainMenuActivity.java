package com.ttyrovou.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import timber.log.Timber;

public class MainMenuActivity extends AppCompatActivity {

    public static final String NUM_ROWS_KEY = "num-rows";
    public static final String NUM_COLS_KEY = "num-cols";
    public static final String NUM_PLAYERS_KEY = "num-players";
    public static final String NUM_LADDERS_KEY = "num-ladders";
    public static final String NUM_SNAKES_KEY = "num-snakes";
    public static final String NUM_APPLES_KEY = "num-apples";

    private EditText numRowsEdittext, numColsEdittext, numPlayersEdittext,
            numLaddersEdittext, numSnakesEdittext, numApplesEdittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Timber.plant(new Timber.DebugTree());

        numRowsEdittext = findViewById(R.id.board_rows_edittext);
        numColsEdittext = findViewById(R.id.board_columns_edittext);
        numPlayersEdittext = findViewById(R.id.num_players_editview);
        numLaddersEdittext = findViewById(R.id.num_ladders_editview);
        numSnakesEdittext = findViewById(R.id.num_snakes_editview);
        numApplesEdittext = findViewById(R.id.num_apples_editview);
    }

    public void startGame(View view) {
        int numRows = Integer.parseInt(numRowsEdittext.getText().toString());
        int numCols = Integer.parseInt(numColsEdittext.getText().toString());
        int numPlayers = Integer.parseInt(numPlayersEdittext.getText().toString());
        int numLadders = Integer.parseInt(numLaddersEdittext.getText().toString());
        int numSnakes = Integer.parseInt(numSnakesEdittext.getText().toString());
        int numApples = Integer.parseInt(numApplesEdittext.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(NUM_ROWS_KEY, numRows);
        intent.putExtra(NUM_COLS_KEY, numCols);
        intent.putExtra(NUM_PLAYERS_KEY, numPlayers);
        intent.putExtra(NUM_LADDERS_KEY, numLadders);
        intent.putExtra(NUM_SNAKES_KEY, numSnakes);
        intent.putExtra(NUM_APPLES_KEY, numApples);
        startActivity(intent);
    }
}
