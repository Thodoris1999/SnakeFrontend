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

    private EditText numRowsEdittext, numColsEdittext, numPlayersEdittext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Timber.plant(new Timber.DebugTree());

        numRowsEdittext = findViewById(R.id.board_rows_edittext);
        numColsEdittext = findViewById(R.id.board_columns_edittext);
        numPlayersEdittext = findViewById(R.id.num_players_editview);
    }

    public void startGame(View view) {
        int numRows = Integer.parseInt(numRowsEdittext.getText().toString());
        int numCols = Integer.parseInt(numColsEdittext.getText().toString());
        int numPlayers = Integer.parseInt(numPlayersEdittext.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(NUM_ROWS_KEY, numRows);
        intent.putExtra(NUM_COLS_KEY, numCols);
        intent.putExtra(NUM_PLAYERS_KEY, numPlayers);
        startActivity(intent);
    }
}
