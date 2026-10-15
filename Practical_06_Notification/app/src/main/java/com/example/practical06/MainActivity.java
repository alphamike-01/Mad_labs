package com.example.practical06;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * Practical 06: Android Notifications
 * Demonstrates creating an Android Notification with title, body, small icon,
 * NotificationChannel, runtime permission handling, and PendingIntent action opening a target activity.
 */
public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "practical_06_channel";
    public static final String CHANNEL_NAME = "College Practical Alerts";
    public static final int NOTIFICATION_ID = 601;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private Button btnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNotify = findViewById(R.id.btnNotify);

        // 1. Create Notification Channel for Android 8.0+ (API 26+)
        createNotificationChannel();

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndShowNotification();
            }
        });
    }

    /**
     * Creates the notification channel required for Android O (API 26) and above.
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications channel for MAD Practical 06 demonstration");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Checks POST_NOTIFICATIONS runtime permission for Android 13+ (API 33+).
     */
    private void checkPermissionAndShowNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE
                );
                return;
            }
        }
        showNotification();
    }

    /**
     * Builds and issues the Android notification with PendingIntent.
     */
    private void showNotification() {
        // Intent that opens NotificationDetailActivity when the notification is clicked
        Intent intent = new Intent(this, NotificationDetailActivity.class);
        intent.putExtra("EXTRA_NOTIFICATION_PAYLOAD", "Notification was triggered at: " + new java.util.Date().toString());
        intent.putExtra("EXTRA_NOTIF_ID", NOTIFICATION_ID);

        // PendingIntent flags for modern Android security
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);

        // Construct notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("MAD Practical 06: New Alert")
                .setContentText("Tap to view details in NotificationDetailActivity.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("This notification was generated from MainActivity. Tapping this notification invokes a PendingIntent that opens NotificationDetailActivity."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, builder.build());
            Toast.makeText(this, "Notification posted! Check your status bar.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission granted!", Toast.LENGTH_SHORT).show();
                showNotification();
            } else {
                Toast.makeText(this, "Permission denied. Notifications cannot be shown.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
