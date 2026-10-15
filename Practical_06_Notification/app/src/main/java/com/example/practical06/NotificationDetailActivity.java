package com.example.practical06;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity opened when the user clicks the notification.
 */
public class NotificationDetailActivity extends AppCompatActivity {

    private TextView tvPayload;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        tvPayload = findViewById(R.id.tvPayload);
        btnBack = findViewById(R.id.btnBack);

        // Retrieve payload extras passed via PendingIntent
        String payload = getIntent().getStringExtra("EXTRA_NOTIFICATION_PAYLOAD");
        int notifId = getIntent().getIntExtra("EXTRA_NOTIF_ID", -1);

        if (payload != null) {
            tvPayload.setText("Notification ID: " + notifId + "\n\n" + payload);
        } else {
            tvPayload.setText("Opened directly without notification payload.");
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
