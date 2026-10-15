package com.example.practical04;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * SecondActivity receives and displays data passed from MainActivity via Intent Extras.
 */
public class SecondActivity extends AppCompatActivity {

    private TextView tvReceivedName, tvReceivedRoll;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tvReceivedName = findViewById(R.id.tvReceivedName);
        tvReceivedRoll = findViewById(R.id.tvReceivedRoll);
        btnBack = findViewById(R.id.btnBack);

        // Retrieve the incoming Intent
        Intent incomingIntent = getIntent();
        if (incomingIntent != null) {
            // Extract String extra
            String name = incomingIntent.getStringExtra(MainActivity.EXTRA_NAME);
            // Extract Integer extra with default fallback of -1
            int rollNumber = incomingIntent.getIntExtra(MainActivity.EXTRA_ROLL, -1);

            // Safe validation of received data
            if (name != null && !name.isEmpty()) {
                tvReceivedName.setText(name);
            } else {
                tvReceivedName.setText("No Name Provided (null)");
            }

            if (rollNumber != -1) {
                tvReceivedRoll.setText(String.valueOf(rollNumber));
            } else {
                tvReceivedRoll.setText("No Roll Number Provided (-1)");
            }
        } else {
            Toast.makeText(this, "No Intent data received!", Toast.LENGTH_SHORT).show();
        }

        // Return button finishes this activity and resumes MainActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // =========================================================================
    // Options Menu for Returning to Main Activity
    // =========================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_back_main) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
