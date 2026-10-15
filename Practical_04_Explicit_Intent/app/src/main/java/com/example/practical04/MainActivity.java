package com.example.practical04;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Practical 04: Explicit Intent Navigation
 * Demonstrates navigation between two activities using an explicit intent,
 * passing String and Integer intent extras, and switching activities via an options menu.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.example.practical04.EXTRA_NAME";
    public static final String EXTRA_ROLL = "com.example.practical04.EXTRA_ROLL";

    private EditText etStudentName, etStudentRoll;
    private Button btnSendData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStudentName = findViewById(R.id.etStudentName);
        etStudentRoll = findViewById(R.id.etStudentRoll);
        btnSendData = findViewById(R.id.btnSendData);

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSecondActivityWithData();
            }
        });
    }

    /**
     * Reads and validates input fields, packages extras, and starts SecondActivity.
     */
    private void navigateToSecondActivityWithData() {
        String name = etStudentName.getText().toString().trim();
        String rollStr = etStudentRoll.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etStudentName.setError("Please enter student name");
            etStudentName.requestFocus();
            Toast.makeText(this, "Student name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(rollStr)) {
            etStudentRoll.setError("Please enter roll number");
            etStudentRoll.requestFocus();
            Toast.makeText(this, "Roll number cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int rollNumber;
        try {
            rollNumber = Integer.parseInt(rollStr);
        } catch (NumberFormatException e) {
            etStudentRoll.setError("Please enter a valid numeric integer");
            etStudentRoll.requestFocus();
            return;
        }

        // Create Explicit Intent pointing directly to SecondActivity.class
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(EXTRA_ROLL, rollNumber);

        Toast.makeText(this, "Navigating with explicit intent...", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    // =========================================================================
    // Options Menu for Switching Activities
    // =========================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_switch_activity) {
            // Options menu allows switching activities with default/current values
            String name = etStudentName.getText().toString().trim();
            String rollStr = etStudentRoll.getText().toString().trim();

            if (TextUtils.isEmpty(name)) name = "Student (Default via Menu)";
            int roll = 101;
            try {
                if (!TextUtils.isEmpty(rollStr)) {
                    roll = Integer.parseInt(rollStr);
                }
            } catch (NumberFormatException ignored) {}

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(EXTRA_NAME, name);
            intent.putExtra(EXTRA_ROLL, roll);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
