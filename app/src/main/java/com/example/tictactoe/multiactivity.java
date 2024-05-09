package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

    private void showGameOverDialog(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.game_over_dialog);

        TextView text = dialog.findViewById(R.id.gameOverText);
        text.setText(message);

        Button dialogButton = dialog.findViewById(R.id.continueButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gameReset();
            }
        });

        dialog.show();
    }

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        // Check if game is not already over
        if (!gameActive) {
            gameReset();
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
                        winnerStr = "Congratulations, X has won";
                        playerXWins++;
                        playerXWinsTextView.setText("X : " + playerXWins);
                    } else {
                        winnerStr = "Congratulations, O has won";
                        playerOWins++;
                        playerOWinsTextView.setText("O : " + playerOWins);
                    }
                    showGameOverDialog(winnerStr);
                    return;
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
                showGameOverDialog("Game Tied");
                gameActive = false;
            }
        }
    }

    public void gameReset() {
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
            ImageView img = findViewById(getImageViewIdByIndex(i));
            if (img != null) {
                img.setImageResource(0);
            }
        }
        status.setText("X's Turn - Tap to play");
    }

    private int getImageViewIdByIndex(int index) {
        switch (index) {
            case 0: return R.id.imageView10;
            case 1: return R.id.imageView11;
            case 2: return R.id.imageView12;
            case 3: return R.id.imageView7;
            case 4: return R.id.imageView8;
            case 5: return R.id.imageView9;
            case 6: return R.id.imageView6;
            case 7: return R.id.imageView5;
            case 8: return R.id.imageView4;
            default: return -1;  // Invalid index
        }
    }
}
