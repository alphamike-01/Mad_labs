package com.example.practical08;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Practical 08: Location Awareness, Sensors, and Native Hardware Access
 * Demonstrates:
 * 1. Location awareness using LocationManager & LocationListener (GPS / Network).
 * 2. Real-time sensor monitoring using SensorManager & SensorEventListener (Accelerometer).
 * 3. Native hardware access using Vibrator, CameraManager (Torch/Flashlight), and BatteryManager.
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private static final int PERMISSION_LOCATION_REQUEST_CODE = 801;

    // UI Elements
    private TextView tvLocationData;
    private TextView tvSensorInfo, tvSensorValues, tvShakeStatus;
    private TextView tvBatteryStatus;
    private Button btnLocation, btnVibrate, btnTorch;

    // Subsystem Managers
    private LocationManager locationManager;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Vibrator vibrator;
    private CameraManager cameraManager;
    private String cameraId;

    private boolean isTorchOn = false;
    private boolean isListeningLocation = false;
    private float lastX, lastY, lastZ;
    private long lastSensorUpdate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Views
        tvLocationData = findViewById(R.id.tvLocationData);
        tvSensorInfo = findViewById(R.id.tvSensorInfo);
        tvSensorValues = findViewById(R.id.tvSensorValues);
        tvShakeStatus = findViewById(R.id.tvShakeStatus);
        tvBatteryStatus = findViewById(R.id.tvBatteryStatus);
        btnLocation = findViewById(R.id.btnLocation);
        btnVibrate = findViewById(R.id.btnVibrate);
        btnTorch = findViewById(R.id.btnTorch);

        // 1. Initialize Subsystems
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Initialize Accelerometer Sensor
        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometerSensor != null) {
                tvSensorInfo.setText("Sensor: " + accelerometerSensor.getName() + " (" + accelerometerSensor.getVendor() + ")");
            } else {
                tvSensorInfo.setText("Accelerometer sensor not available on this device.");
            }
        }

        // Initialize Camera Flashlight hardware
        initCameraFlashlight();

        // Query Battery Hardware Status
        readBatteryHardwareStatus();

        // 2. Setup Button Click Listeners
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndRequestLocation();
            }
        });

        btnVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerHapticFeedback();
            }
        });

        btnTorch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlashlight();
            }
        });
    }

    // =========================================================================
    // 1. Location Awareness Subsystem
    // =========================================================================

    private void checkPermissionAndRequestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE
            );
            return;
        }

        fetchCurrentLocation();
    }

    private void fetchCurrentLocation() {
        if (locationManager == null) return;

        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGpsEnabled && !isNetworkEnabled) {
            tvLocationData.setText("Location Error:\nLocation Providers are disabled. Please enable GPS in device settings.");
            Toast.makeText(this, "Please enable GPS / Location in Settings", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // Check last known location first for immediate feedback
            Location lastKnown = null;
            if (isGpsEnabled) {
                lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (lastKnown == null && isNetworkEnabled) {
                lastKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (lastKnown != null) {
                updateLocationUI(lastKnown);
            }

            // Register for live updates (minTime: 2000ms, minDistance: 1m)
            if (isGpsEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1.0f, this);
                isListeningLocation = true;
            } else if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1.0f, this);
                isListeningLocation = true;
            }

            Toast.makeText(this, "Acquiring live GPS / Network coordinates...", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            tvLocationData.setText("SecurityException: " + e.getMessage());
        }
    }

    private void updateLocationUI(Location loc) {
        String data = "Latitude: " + loc.getLatitude() + "\n"
                + "Longitude: " + loc.getLongitude() + "\n"
                + "Altitude: " + (loc.hasAltitude() ? loc.getAltitude() + " m" : "N/A") + "\n"
                + "Accuracy: ±" + loc.getAccuracy() + " m\n"
                + "Provider: " + loc.getProvider().toUpperCase();
        tvLocationData.setText(data);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateLocationUI(location);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Toast.makeText(this, "Location Provider Enabled: " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Toast.makeText(this, "Location Provider Disabled: " + provider, Toast.LENGTH_SHORT).show();
    }

    // =========================================================================
    // 2. Sensor Monitoring Subsystem (Accelerometer)
    // =========================================================================

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastSensorUpdate) > 100) { // Update UI at ~10Hz
                long diffTime = currentTime - lastSensorUpdate;
                lastSensorUpdate = currentTime;

                tvSensorValues.setText(String.format("X-axis (Lateral): %.2f m/s²\nY-axis (Longitudinal): %.2f m/s²\nZ-axis (Vertical): %.2f m/s²", x, y, z));

                // Shake detection algorithm: calculate speed of change
                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;
                if (speed > 800) {
                    tvShakeStatus.setText("Shake state: SHAKE DETECTED! (Speed: " + (int) speed + ")");
                    tvShakeStatus.setTextColor(ContextCompat.getColor(this, R.color.red_700));
                } else {
                    tvShakeStatus.setText("Shake state: Device at rest / steady");
                    tvShakeStatus.setTextColor(ContextCompat.getColor(this, R.color.green_700));
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    // =========================================================================
    // 3. Native Hardware Access (Vibrator, Flashlight, Battery)
    // =========================================================================

    private void triggerHapticFeedback() {
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Predefined haptic click effect
                VibrationEffect effect = VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.vibrate(effect);
            } else {
                // Deprecated method for older Android versions
                vibrator.vibrate(150);
            }
            Toast.makeText(this, "Haptic vibration pulse triggered!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vibrator hardware not supported on device/emulator", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCameraFlashlight() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            try {
                if (cameraManager != null) {
                    String[] ids = cameraManager.getCameraIdList();
                    if (ids.length > 0) {
                        cameraId = ids[0];
                    }
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleFlashlight() {
        if (cameraId == null) {
            Toast.makeText(this, "Flashlight hardware not available on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isTorchOn = !isTorchOn;
                cameraManager.setTorchMode(cameraId, isTorchOn);
                btnTorch.setText(isTorchOn ? "Turn Torch OFF" : "Turn Torch ON");
                Toast.makeText(this, "Flashlight is " + (isTorchOn ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "CameraManager torch mode requires Android 6.0+", Toast.LENGTH_SHORT).show();
            }
        } catch (CameraAccessException e) {
            Toast.makeText(this, "Flashlight Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void readBatteryHardwareStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

            int batteryPct = (int) ((level / (float) scale) * 100);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
                    || status == BatteryManager.BATTERY_STATUS_FULL;

            tvBatteryStatus.setText("Hardware Battery: " + batteryPct + "% (" + (isCharging ? "Charging" : "Discharging") + ")");
        } else {
            tvBatteryStatus.setText("Battery status: Unavailable");
        }
    }

    // =========================================================================
    // Activity Lifecycle Management
    // =========================================================================

    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listener in onResume to conserve battery
        if (sensorManager != null && accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listener in onPause to stop sensor battery drain
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        // Remove location updates when activity is paused
        if (locationManager != null && isListeningLocation) {
            locationManager.removeUpdates(this);
            isListeningLocation = false;
        }
        // Turn off torch if app goes to background
        if (isTorchOn) {
            toggleFlashlight();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show();
                fetchCurrentLocation();
            } else {
                tvLocationData.setText("Location Permission Denied.\nCannot retrieve GPS or Network coordinates.");
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
