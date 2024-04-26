package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button playButton = findViewById(R.id.button2);
        Button exitButton = findViewById(R.id.button4);
        Button singlePlayerButton = findViewById(R.id.button); // Added
        Button aboutButton = findViewById(R.id.button3); // Added

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(home.this, multiactivity.class);
                startActivity(gameIntent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Start MainActivity2 (multiactivity) when "Single Player" button is clicked
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singlePlayerIntent = new Intent(home.this, MainActivity2.class); // Changed
                startActivity(singlePlayerIntent);
            }
        });

        // Start AboutActivity when "About us" button is clicked
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(home.this, about.class); // Change to your AboutActivity class name
                startActivity(aboutIntent);
            }
        });
    }
}
