package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class multiactivity extends AppCompatActivity {

    boolean gameActive = true;
    // Player State
    // 0 = X
    // 1 = O
    int activePlayer = 0;
    int[] gameState = { 2, 2, 2, 2, 2, 2, 2, 2, 2 };
    // State meaning
    // 0 = X
    // 1 = O
    // 2 = null
    int[][] winPositions = {{0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}};

    TextView status;
    TextView playerXWinsTextView;
    TextView playerOWinsTextView;

    int playerXWins = 0;
    int playerOWins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);
        playerXWinsTextView = findViewById(R.id.playerXWins);
        playerOWinsTextView = findViewById(R.id.playerOWins);
    }

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        // Check if game is not already over
        if (!gameActive) {
            gameReset(view);
        }

        // Check if tapped position is empty
        if (gameState[tappedImage] == 2) {
            gameState[tappedImage] = activePlayer;
            img.setTranslationY(-1000f);
            if (activePlayer == 0) {
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                status.setText("O's Turn - Tap to play");
            } else {
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                status.setText("X's Turn - Tap to play");
            }
            img.animate().translationYBy(1000f).setDuration(300);

            // Check for a win
            for (int[] winPosition : winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                        gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[0]] != 2) {
                    // Somebody has won! - Find out who!
                    String winnerStr;
                    gameActive = false;
                    if (gameState[winPosition[0]] == 0) {
                        winnerStr = "X has won";
                        playerXWins++;
                        playerXWinsTextView.setText("X : " + playerXWins);
                    } else {
                        winnerStr = "O has won";
                        playerOWins++;
                        playerOWinsTextView.setText("O : " + playerOWins);
                    }
                    status.setText(winnerStr);
                    return; // Exit method to prevent further actions
                }
            }

            // Check for a tie
            boolean gameOver = true;
            for (int state : gameState) {
                if (state == 2) {
                    gameOver = false;
                    break;
                }
            }
            if (gameOver) {
                status.setText("Game Tied");
                gameActive = false;
            }
        }
    }

    public void gameReset(View view) {
        gameActive = true;
        activePlayer = 0;
        for(int i=0; i<gameState.length; i++){
            gameState[i] = 2;
        }
        ((ImageView)findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView8)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView9)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView10)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView12)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView11)).setImageResource(0);

        // Clear the status text to indicate the start of a new game
        status.setText("X's Turn - Tap to play");
    }
}
