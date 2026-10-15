package com.example.practical07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Custom BroadcastReceiver that listens for custom broadcast action.
 * Displays a Toast message and logs receipt details in Logcat.
 */
public class CustomBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "CustomBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && MainActivity.ACTION_CUSTOM_BROADCAST.equals(intent.getAction())) {
            String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
            if (message == null || message.isEmpty()) {
                message = "Default broadcast payload (empty extra)";
            }

            // 1. Output to Logcat
            Log.d(TAG, "=================================================");
            Log.d(TAG, "Custom Broadcast Successfully Received!");
            Log.d(TAG, "Action: " + intent.getAction());
            Log.d(TAG, "Message: " + message);
            Log.d(TAG, "Timestamp: " + new java.util.Date().toString());
            Log.d(TAG, "=================================================");

            // 2. Display Toast message
            String toastText = "Broadcast Received:\n" + message;
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        }
    }
}
