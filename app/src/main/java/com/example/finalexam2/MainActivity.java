package com.example.finalexam2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Use Handler to delay the transition to LoginActivity
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Create an Intent to start the LoginActivity
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);

                // Close the MainActivity to prevent going back
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}