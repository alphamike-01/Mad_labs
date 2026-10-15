package com.example.practical11;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Practical 11: Dynamic Item Generation
 * Accepts an integer count from the user, validates bounds, and transmits
 * the count via Intent extra to DynamicItemsActivity.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_COUNT = "com.example.practical11.EXTRA_ITEM_COUNT";
    private static final int MAX_ALLOWED_ITEMS = 100;

    private EditText etItemCount;
    private Button btnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etItemCount = findViewById(R.id.etItemCount);
        btnGenerate = findViewById(R.id.btnGenerate);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLaunch();
            }
        });
    }

    private void validateAndLaunch() {
        String inputStr = etItemCount.getText().toString().trim();

        if (TextUtils.isEmpty(inputStr)) {
            etItemCount.setError("Please enter the number of items");
            etItemCount.requestFocus();
            Toast.makeText(this, "Number field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        int count;
        try {
            count = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
            etItemCount.setError("Invalid number format");
            etItemCount.requestFocus();
            return;
        }

        // Validate range bounds
        if (count <= 0) {
            etItemCount.setError("Please enter a positive number greater than 0");
            etItemCount.requestFocus();
            Toast.makeText(this, "Item count must be at least 1", Toast.LENGTH_SHORT).show();
            return;
        }

        if (count > MAX_ALLOWED_ITEMS) {
            etItemCount.setError("Safety cap: Maximum allowed is " + MAX_ALLOWED_ITEMS + " items");
            etItemCount.requestFocus();
            Toast.makeText(this, "Exceeds maximum limit (" + MAX_ALLOWED_ITEMS + ")", Toast.LENGTH_LONG).show();
            return;
        }

        // Pass count to destination activity
        Intent intent = new Intent(MainActivity.this, DynamicItemsActivity.class);
        intent.putExtra(EXTRA_ITEM_COUNT, count);
        startActivity(intent);
    }
}
