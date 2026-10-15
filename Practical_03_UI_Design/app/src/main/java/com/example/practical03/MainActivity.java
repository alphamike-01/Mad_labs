package com.example.practical03;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Practical 03: UI Design
 * Demonstrates designing a user interface using ConstraintLayout, LinearLayout,
 * TextView, EditText, Button, ImageView, custom drawable resources,
 * and an Options Menu with a functional Settings item.
 */
public class MainActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private TextView tvTitle, tvSubtitle, tvResult;
    private EditText etName, etEmail;
    private Button btnSubmit, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components by binding their XML IDs
        ivAvatar = findViewById(R.id.ivAvatar);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnClear = findViewById(R.id.btnClear);
        tvResult = findViewById(R.id.tvResult);

        // Submit Button Click Listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFormSubmission();
            }
        });

        // Clear Button Click Listener
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });
    }

    /**
     * Validates user inputs and displays the submitted data in tvResult.
     */
    private void handleFormSubmission() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // 1. Validate Name
        if (TextUtils.isEmpty(name)) {
            etName.setError("Full name is required!");
            etName.requestFocus();
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Validate Email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email address is required!");
            etEmail.requestFocus();
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address!");
            etEmail.requestFocus();
            Toast.makeText(this, "Invalid email format!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear any previous error indicators
        etName.setError(null);
        etEmail.setError(null);

        // Display results in TextView summary
        String summary = "Status: Successfully Registered\n"
                + "Student Name: " + name + "\n"
                + "College Email: " + email + "\n"
                + "Timestamp: " + new java.util.Date().toString();
        tvResult.setText(summary);

        Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Resets input fields and output summary.
     */
    private void clearForm() {
        etName.setText("");
        etEmail.setText("");
        etName.setError(null);
        etEmail.setError(null);
        tvResult.setText(R.string.label_result_placeholder);
        Toast.makeText(this, "Form cleared", Toast.LENGTH_SHORT).show();
    }

    // =========================================================================
    // Options Menu Implementation
    // =========================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML resource
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        } else if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Displays a functional Settings dialog with selectable configuration options.
     */
    private void showSettingsDialog() {
        final String[] settingsOptions = {
                "Push Notifications: Enabled",
                "Theme: System Default",
                "Data Saver: Disabled",
                "Clear Cached Form Data"
        };
        final boolean[] checkedItems = {true, false, false, false};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Application Settings");
        builder.setIcon(R.drawable.ic_student_avatar);
        builder.setMultiChoiceItems(settingsOptions, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        });
        builder.setPositiveButton("Save Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Settings preferences saved!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /**
     * Displays the About dialog.
     */
    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About Practical 03")
                .setMessage("Practical 03: Native UI Design\nVersion: 1.0\nBuilt using Native Android XML layouts and Java.")
                .setPositiveButton("OK", null)
                .show();
    }
}
