package com.example.practical07;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Practical 07: Custom BroadcastReceiver
 * Demonstrates dynamically registering a custom BroadcastReceiver,
 * sending an explicit custom broadcast, displaying a Toast, and logging to Logcat.
 */
public class MainActivity extends AppCompatActivity {

    public static final String ACTION_CUSTOM_BROADCAST = "com.example.practical07.CUSTOM_BROADCAST";
    public static final String EXTRA_MESSAGE = "com.example.practical07.EXTRA_MESSAGE";

    private CustomBroadcastReceiver receiver;
    private boolean isReceiverRegistered = false;

    private TextView tvReceiverStatus;
    private EditText etBroadcastMessage;
    private Button btnSendBroadcast, btnRegister, btnUnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvReceiverStatus = findViewById(R.id.tvReceiverStatus);
        etBroadcastMessage = findViewById(R.id.etBroadcastMessage);
        btnSendBroadcast = findViewById(R.id.btnSendBroadcast);
        btnRegister = findViewById(R.id.btnRegister);
        btnUnregister = findViewById(R.id.btnUnregister);

        receiver = new CustomBroadcastReceiver();

        // Register receiver dynamically by default
        registerCustomReceiver();

        btnSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCustomBroadcast();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCustomReceiver();
            }
        });

        btnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unregisterCustomReceiver();
            }
        });
    }

    /**
     * Sends the custom broadcast. Uses explicit broadcast by setting package name
     * to comply with Android 8.0+ background broadcast limitations.
     */
    private void sendCustomBroadcast() {
        String msg = etBroadcastMessage.getText().toString().trim();
        if (TextUtils.isEmpty(msg)) {
            msg = "Default Broadcast Message";
        }

        Intent broadcastIntent = new Intent(ACTION_CUSTOM_BROADCAST);
        broadcastIntent.putExtra(EXTRA_MESSAGE, msg);
        // Explicitly target this package to comply with modern Android security restrictions
        broadcastIntent.setPackage(getPackageName());

        sendBroadcast(broadcastIntent);
        Toast.makeText(this, "Broadcast sent: " + msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Dynamically registers the custom receiver with modern RECEIVER_NOT_EXPORTED flag.
     */
    private void registerCustomReceiver() {
        if (!isReceiverRegistered) {
            IntentFilter filter = new IntentFilter(ACTION_CUSTOM_BROADCAST);
            ContextCompat.registerReceiver(
                    this,
                    receiver,
                    filter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
            );
            isReceiverRegistered = true;
            tvReceiverStatus.setText("Status: Receiver is REGISTERED");
            tvReceiverStatus.setTextColor(ContextCompat.getColor(this, R.color.green_700));
            Toast.makeText(this, "Receiver Registered", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Receiver is already registered", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Unregisters the custom receiver safely.
     */
    private void unregisterCustomReceiver() {
        if (isReceiverRegistered) {
            unregisterReceiver(receiver);
            isReceiverRegistered = false;
            tvReceiverStatus.setText("Status: Receiver is UNREGISTERED");
            tvReceiverStatus.setTextColor(ContextCompat.getColor(this, R.color.red_700));
            Toast.makeText(this, "Receiver Unregistered", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Receiver is not currently registered", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister on destroy to prevent receiver leaks
        if (isReceiverRegistered) {
            unregisterReceiver(receiver);
            isReceiverRegistered = false;
        }
    }
}
