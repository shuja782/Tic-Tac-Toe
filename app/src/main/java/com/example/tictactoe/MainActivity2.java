package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
            gameReset(view);
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

    private void checkGameState() {
        // Check for a win
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] != 2 &&
                    gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]]) {
                // A player has won
                gameActive = false;
                if (gameState[winPosition[0]] == 0) {
                    // Player X wins
                    playerXWins++;
                    playerXWinsTextView.setText("X : " + playerXWins);
                    statusTextView.setText(" X wins");
                } else {
                    // Player O (or AI) wins
                    playerOWins++;
                    playerOWinsTextView.setText("AI : " + playerOWins);
                    statusTextView.setText(" AI wins");
                }
                return;
            }
        }

        // Check for a tie
        boolean isTie = true;
        for (int state : gameState) {
            if (state == 2) {
                // There are still empty cells, game is not a tie
                isTie = false;
                break;
            }
        }
        if (isTie) {
            // Game is a tie, no player wins
            gameActive = false;
            statusTextView.setText("Game Tied");
        }
    }



    private int minimax(int[] board, int depth, boolean isMaximizing) {
        int score = evaluate(board);

        if (score != 0 || depth == 9) {
            return score;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 2) {
                    board[i] = 1;
                    bestScore = Math.max(bestScore, minimax(board, depth + 1, false));
                    board[i] = 2; // Undo move
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 2) {
                    board[i] = 0;
                    bestScore = Math.min(bestScore, minimax(board, depth + 1, true));
                    board[i] = 2; // Undo move
                }
            }
            return bestScore;
        }
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
            if (gameState[i] == 2) {
                gameState[i] = 1; // Assume AI's move
                int score = minimax(gameState, 0, false);
                gameState[i] = 2; // Undo move

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            gameState[bestMove] = 1; // Make AI's move
            updateCellUI(bestMove); // Update UI with AI's move
            statusTextView.setText("AI's Turn"); // Update status text
            checkGameState(); // Check game state after AI's move
            activePlayer = 0;
        }
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




    private void gameReset(View view) {
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
        statusTextView.setText("X's Turn - Tap to play");
    }
}

