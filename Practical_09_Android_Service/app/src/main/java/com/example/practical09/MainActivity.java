package com.example.practical09;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * MainActivity provides controls to start and stop MyCustomService
 * and observe lifecycle transitions.
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvServiceStatus;
    private Button btnStartService, btnStopService;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvServiceStatus = findViewById(R.id.tvServiceStatus);
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);

        serviceIntent = new Intent(this, MyCustomService.class);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starts the service
                startService(serviceIntent);
                tvServiceStatus.setText("Status: Service is RUNNING");
                tvServiceStatus.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.green_700));
                Toast.makeText(MainActivity.this, "startService() requested", Toast.LENGTH_SHORT).show();
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stops the service
                stopService(serviceIntent);
                tvServiceStatus.setText("Status: Service is STOPPED");
                tvServiceStatus.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.red_700));
                Toast.makeText(MainActivity.this, "stopService() requested", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
