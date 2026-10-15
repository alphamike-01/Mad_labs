package com.example.practical09;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Practical 09: Android Started Service.
 * Demonstrates Service lifecycle methods: onCreate(), onStartCommand(), and onDestroy()
 * with comprehensive logging to Logcat.
 */
public class MyCustomService extends Service {

    public static final String TAG = "MyCustomService";

    private boolean isRunning = false;
    private Thread workerThread;
    private int counter = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        // Called once when service is first created
        Log.d(TAG, "--------------------------------------------------");
        Log.d(TAG, "[LIFECYCLE] onCreate() invoked! Service instance created.");
        Log.d(TAG, "Thread: " + Thread.currentThread().getName());
        Log.d(TAG, "--------------------------------------------------");
        Toast.makeText(this, "Service: onCreate() called", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Called each time startService() is requested
        Log.d(TAG, "[LIFECYCLE] onStartCommand() invoked! startId = " + startId + ", flags = " + flags);
        Toast.makeText(this, "Service: onStartCommand() [ID: " + startId + "]", Toast.LENGTH_SHORT).show();

        // Start background worker thread if not already running
        if (!isRunning) {
            isRunning = true;
            workerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning) {
                        try {
                            counter++;
                            Log.d(TAG, "[RUNNING] Background task heartbeat #" + counter + " (Thread: " + Thread.currentThread().getName() + ")");
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "[THREAD] Background worker thread interrupted.");
                            break;
                        }
                    }
                }
            });
            workerThread.start();
        }

        // START_STICKY tells system to recreate service if terminated by system
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Called when stopService() or stopSelf() is executed
        isRunning = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }

        Log.d(TAG, "--------------------------------------------------");
        Log.d(TAG, "[LIFECYCLE] onDestroy() invoked! Service terminated.");
        Log.d(TAG, "Total heartbeat count logged: " + counter);
        Log.d(TAG, "--------------------------------------------------");
        Toast.makeText(this, "Service: onDestroy() called", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Return null for Started Service
        return null;
    }
}
