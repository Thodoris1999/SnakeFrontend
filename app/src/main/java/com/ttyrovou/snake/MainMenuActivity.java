package com.ttyrovou.snake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import main.GameConfig;
import main.PlayerType;
import timber.log.Timber;

public class MainMenuActivity extends AppCompatActivity {

    public static final String GAME_CONFIG_KEY = "game-config key";

    private EditText numRowsEdittext, numColsEdittext, numPlayersEdittext,
            numLaddersEdittext, numSnakesEdittext, numApplesEdittext;
    private Spinner player1TypeSpinner, player2TypeSpinner;

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

        player1TypeSpinner = findViewById(R.id.player1_type);
        player2TypeSpinner = findViewById(R.id.player2_type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.player_type_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        player1TypeSpinner.setAdapter(adapter);
        player2TypeSpinner.setAdapter(adapter);
    }

    public void startGame(View view) {
        int numRows = Integer.parseInt(numRowsEdittext.getText().toString());
        int numCols = Integer.parseInt(numColsEdittext.getText().toString());
        int numPlayers = Integer.parseInt(numPlayersEdittext.getText().toString());
        int numLadders = Integer.parseInt(numLaddersEdittext.getText().toString());
        int numSnakes = Integer.parseInt(numSnakesEdittext.getText().toString());
        int numApples = Integer.parseInt(numApplesEdittext.getText().toString());

        PlayerType[] playerTypes = new PlayerType[numPlayers];
        if (numPlayers == 2) {
            if (player1TypeSpinner.getSelectedItem().equals("Normal player")) {
                playerTypes[0] = PlayerType.NORMAL;
            } else if (player1TypeSpinner.getSelectedItem().equals("Heuristic player")) {
                playerTypes[0] = PlayerType.HEURISTIC;
            } else if (player1TypeSpinner.getSelectedItem().equals("Minmax player")) {
                playerTypes[0] = PlayerType.MINMAX;
            }
            if (player2TypeSpinner.getSelectedItem().equals("Normal player")) {
                playerTypes[1] = PlayerType.NORMAL;
            } else if (player2TypeSpinner.getSelectedItem().equals("Heuristic player")) {
                playerTypes[1] = PlayerType.HEURISTIC;
            } else if (player2TypeSpinner.getSelectedItem().equals("Minmax player")) {
                playerTypes[1] = PlayerType.MINMAX;
            }
        } else {
            for (int i = 0; i < numPlayers; i++) {
                playerTypes[i] = PlayerType.NORMAL;
            }
        }
        GameConfig gameConfig = new GameConfig(numRows, numCols, 30, playerTypes);
        gameConfig.setNumLadders(numLadders);
        gameConfig.setNumSnakes(numSnakes);
        gameConfig.setNumApples(numApples);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(GAME_CONFIG_KEY, gameConfig);
        startActivity(intent);
    }
}
