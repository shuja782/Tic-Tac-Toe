package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    boolean gameActive = true;
    int activePlayer = 0; // 0 for player, 1 for AI
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2}; // 2 represents empty cell
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    TextView statusTextView;
    TextView playerXWinsTextView;
    TextView playerOWinsTextView;

    int playerXWins = 0;
    int playerOWins = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.status);
        playerXWinsTextView = findViewById(R.id.playerXWins);
        playerOWinsTextView = findViewById(R.id.playerOWins);
    }

    public void playerTap(View view) {
        if (!gameActive) {
            gameReset();
            return;
        }

        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (gameState[tappedImage] == 2) {
            gameState[tappedImage] = activePlayer;
            if (activePlayer == 0) {
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                statusTextView.setText("AI's Turn");
                checkGameState();
                if (gameActive)
                    aiPlayerMove();
            }
        }
    }
    private void updateScores() {
        playerXWinsTextView.setText("X : " + playerXWins);
        playerOWinsTextView.setText("O : " + playerOWins);
    }


    private void checkGameState() {
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] != 2 && gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]]) {
                gameActive = false;
                if (gameState[winPosition[0]] == 0) {
                    playerXWins++; // Increment score for Player X
                } else {
                    playerOWins++; // Increment score for AI
                }
                String message = gameState[winPosition[0]] == 0 ? getString(R.string.congratulations) : getString(R.string.better_luck);
                showGameOverDialog(message);
                updateScores(); // Update scores on the UI
                return;
            }
        }

        // Check for a tie scenario
        boolean isTie = true;
        for (int state : gameState) {
            if (state == 2) {
                isTie = false;
                break;
            }
        }
        if (isTie) {
            gameActive = false;
            statusTextView.setText("Game Tied");
            showGameOverDialog("Game Tied");
        }
    }





    private int minimax(int[] board, int depth, boolean isMaximizing) {
        // First, we need to check if the game has already been won by someone (use a new function here)
        int winner = checkWinner(board);
        if (winner != 0 || depth == 9) {
            return winner;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 2) { // If the spot is empty
                    board[i] = 1; // AI's move
                    int score = minimax(board, depth + 1, false);
                    board[i] = 2; // Undo the move
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 2) { // If the spot is empty
                    board[i] = 0; // Player's move
                    int score = minimax(board, depth + 1, true);
                    board[i] = 2; // Undo the move
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    private int checkWinner(int[] board) {
        int[][] winConditions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
        for (int[] winPos : winConditions) {
            if (board[winPos[0]] != 2 && board[winPos[0]] == board[winPos[1]] && board[winPos[1]] == board[winPos[2]]) {
                return board[winPos[0]] == 1 ? 10 : -10;
            }
        }
        return 0;
    }

    private int evaluate(int[] board) {
        int score = 0;
        // Check for AI's winning arrays
        for (int[] winPosition : winPositions) {
            if (board[winPosition[0]] == 1 &&
                    board[winPosition[1]] == 1 &&
                    board[winPosition[2]] == 1) {
                score += 100; // High score for AI's winning arrays
            }
        }
        // Check for user's winning arrays and block them
        for (int[] winPosition : winPositions) {
            if (board[winPosition[0]] == 0 &&
                    board[winPosition[1]] == 0 &&
                    board[winPosition[2]] == 0) {
                score -= 50; // Higher score for blocking user's winning arrays
            }
        }
        return score;
    }


    private void aiPlayerMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < gameState.length; i++) {
            if (gameState[i] == 2) { // Check if the spot is empty
                gameState[i] = 1; // Assume AI's move
                int score = minimax(gameState, 0, false);
                gameState[i] = 2; // Undo the move

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            gameState[bestMove] = 1; // Execute the best move
            updateCellUI(bestMove); // Update UI with AI's move
            statusTextView.setText("Player X's Turn");
            checkGameState(); // Check game state after AI's move
            activePlayer = 0; // Switch turn back to player
        }
    }

    private void showGameOverDialog(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.game_over_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.gameOverText);
        text.setText(message);

        Button dialogButton = (Button) dialog.findViewById(R.id.continueButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                gameReset();
            }
        });

        dialog.show();
    }

    private void updateCellUI(int position) {
        // Find the ImageView corresponding to the position
        ImageView imageView = null;
        switch (position) {
            case 0:
                imageView = findViewById(R.id.imageView10);
                break;
            case 1:
                imageView = findViewById(R.id.imageView11);
                break;
            case 2:
                imageView = findViewById(R.id.imageView12);
                break;
            case 3:
                imageView = findViewById(R.id.imageView7);
                break;
            case 4:
                imageView = findViewById(R.id.imageView8);
                break;
            case 5:
                imageView = findViewById(R.id.imageView9);
                break;
            case 6:
                imageView = findViewById(R.id.imageView6);
                break;
            case 7:
                imageView = findViewById(R.id.imageView5);
                break;
            case 8:
                imageView = findViewById(R.id.imageView4);
                break;
        }

        if (imageView != null) {
            // Update the ImageView with AI's symbol
            imageView.setImageResource(R.drawable.o); // Assuming "o" is the AI's symbol
        }
    }




    private void gameReset() {
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
            ImageView img = findViewById(getImageViewIdByIndex(i));
            if (img != null) {
                img.setImageResource(0);  // Clear the ImageView
            }
        }
        statusTextView.setText("X's Turn - Tap to play");
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

