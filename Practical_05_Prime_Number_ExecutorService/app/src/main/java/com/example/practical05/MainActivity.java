package com.example.practical05;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Practical 05: Background Computation with ExecutorService
 * Demonstrates prime-number verification executed on a background worker thread
 * using Java's ExecutorService, updating the user interface safely via the Main Looper Handler.
 */
public class MainActivity extends AppCompatActivity {

    private EditText etNumber;
    private Button btnCheck;
    private ProgressBar progressBar;
    private TextView tvStatus;

    // Single-thread executor for background processing
    private ExecutorService executorService;
    // Handler bound to the main (UI) thread
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber = findViewById(R.id.etNumber);
        btnCheck = findViewById(R.id.btnCheck);
        progressBar = findViewById(R.id.progressBar);
        tvStatus = findViewById(R.id.tvStatus);

        // Initialize concurrency utilities
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPrimeComputation();
            }
        });
    }

    /**
     * Reads user input and submits computation task to ExecutorService.
     */
    private void startPrimeComputation() {
        String inputStr = etNumber.getText().toString().trim();

        if (TextUtils.isEmpty(inputStr)) {
            etNumber.setError("Please enter an integer");
            etNumber.requestFocus();
            Toast.makeText(this, "Input field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final long candidateNumber;
        try {
            candidateNumber = Long.parseLong(inputStr);
        } catch (NumberFormatException e) {
            etNumber.setError("Number is too large or invalid format");
            etNumber.requestFocus();
            return;
        }

        // Update UI to running state
        progressBar.setVisibility(View.VISIBLE);
        btnCheck.setEnabled(false);
        tvStatus.setText("Computing in background thread for: " + candidateNumber + "...");

        // Submit task to ExecutorService
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                PrimeResult result = computePrime(candidateNumber);
                long duration = System.currentTimeMillis() - startTime;

                // Post results back to Main (UI) thread
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Check if activity is still alive
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }

                        progressBar.setVisibility(View.GONE);
                        btnCheck.setEnabled(true);

                        StringBuilder sb = new StringBuilder();
                        sb.append("Tested Number: ").append(candidateNumber).append("\n");
                        sb.append("Result: ").append(result.isPrime ? "PRIME NUMBER" : "NOT A PRIME (Composite)").append("\n");
                        sb.append("Explanation: ").append(result.explanation).append("\n");
                        sb.append("Computation Time: ").append(duration).append(" ms");

                        tvStatus.setText(sb.toString());

                        if (result.isPrime) {
                            Toast.makeText(MainActivity.this, candidateNumber + " is a Prime Number!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, candidateNumber + " is not a prime.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * Pure background computational method to check primality.
     */
    private PrimeResult computePrime(long n) {
        if (n <= 1) {
            return new PrimeResult(false, n + " is less than or equal to 1. Prime numbers are defined as integers greater than 1.");
        }
        if (n == 2) {
            return new PrimeResult(true, "2 is the smallest and only even prime number.");
        }
        if (n % 2 == 0) {
            return new PrimeResult(false, n + " is divisible by 2.");
        }

        // Test odd factors up to sqrt(n)
        long limit = (long) Math.sqrt(n);
        for (long i = 3; i <= limit; i += 2) {
            if (n % i == 0) {
                return new PrimeResult(false, n + " is divisible by " + i + " (factor: " + i + " * " + (n / i) + ").");
            }
        }

        return new PrimeResult(true, n + " has no divisors other than 1 and itself.");
    }

    /**
     * Clean up executor service on Activity destruction to prevent thread leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    /**
     * Helper model to hold computation results.
     */
    private static class PrimeResult {
        final boolean isPrime;
        final String explanation;

        PrimeResult(boolean isPrime, String explanation) {
            this.isPrime = isPrime;
            this.explanation = explanation;
        }
    }
}
