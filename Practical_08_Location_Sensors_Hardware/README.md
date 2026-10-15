# Practical 08: Location Awareness, Sensors, and Native Hardware Access

## 1. Aim
location awareness/ sensor + native hardwar access

## 2. Theory
Android smartphones embed a wide array of specialized native sensors and hardware peripherals that bridge software applications with physical environment awareness.

### 1. Location Awareness (`LocationManager`):
- **Providers**: Android provides location through two primary hardware/network providers:
  - `LocationManager.GPS_PROVIDER`: Uses satellite navigation signals. Highly accurate (typically 3-10 meters outdoors) but consumes significant battery and requires a clear line of sight to the sky.
  - `LocationManager.NETWORK_PROVIDER`: Uses cellular tower IDs and Wi-Fi access points. Works indoors, acquires positions rapidly, and consumes minimal power, though with lower accuracy (30-200 meters).
- **Runtime Permissions**: Accessing physical device coordinates requires dangerous runtime permissions (`ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION`).

### 2. Sensor Subsystem (`SensorManager`):
- **Sensor Framework**: Part of `android.hardware`, the Android Sensor Framework manages physical sensors (motion, environmental, and position sensors).
- **Accelerometer (`Sensor.TYPE_ACCELEROMETER`)**: Measures acceleration force applied to the device along three physical dimensions (\(X, Y, Z\)) in \(\text{m/s}^2\), including the force of Earth's gravity (\(\approx 9.81\;\text{m/s}^2\) along the \(Z\)-axis when resting flat on a table).
- **Lifecycle Efficiency**: Sensors consume substantial battery power. Sensor listeners must always be registered in `onResume()` and unregistered in `onPause()`.

### 3. Native Hardware Access:
- **Vibrator (`Vibrator` / `VibrationEffect`)**: Controls the device's physical eccentric rotating mass (ERM) or linear resonant actuator (LRA) haptic motor.
- **Camera Flashlight (`CameraManager.setTorchMode`)**: Controls the high-intensity LED flash hardware on the back camera.
- **Battery Hardware (`BatteryManager`)**: Broadcasts hardware battery percentage and power source status.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23
- **Permissions**: `android.permission.ACCESS_FINE_LOCATION`, `android.permission.ACCESS_COARSE_LOCATION`, `android.permission.VIBRATE`, `android.permission.CAMERA`

## 4. Algorithm
1. **Declare Permissions**: In `AndroidManifest.xml`, declare `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `VIBRATE`, and `CAMERA`.
2. **Initialize Managers**: In `onCreate()`, obtain instances of `LocationManager`, `SensorManager`, `Vibrator`, and `CameraManager`.
3. **Register Sensor Listener**:
   - In `onResume()`, register `SensorEventListener` for `Sensor.TYPE_ACCELEROMETER` with rate `SENSOR_DELAY_UI`.
   - In `onSensorChanged()`, read \(X, Y, Z\) values, update UI labels, and compute difference from previous values to detect a shake gesture.
   - In `onPause()`, call `sensorManager.unregisterListener(this)`.
4. **Location Request**:
   - Check `ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)`. If missing, request via `ActivityCompat.requestPermissions()`.
   - If granted, check if GPS or Network providers are enabled.
   - Read `getLastKnownLocation()`, then register `requestLocationUpdates()` for live updates.
   - Update latitude, longitude, altitude, accuracy, and provider in UI.
5. **Haptic Vibration**: On button tap, trigger `vibrator.vibrate(VibrationEffect.createOneShot(150, DEFAULT_AMPLITUDE))`.
6. **Flashlight Toggle**: On button tap, call `cameraManager.setTorchMode(cameraId, isTorchOn)`.
7. **Battery Monitor**: Register for sticky broadcast `Intent.ACTION_BATTERY_CHANGED` to read hardware battery percentage.

## 5. Complete Source Code
See [MainActivity.java](file:///app/src/main/java/com/example/practical08/MainActivity.java).

## 6. XML Layouts
See [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml).

## 7. AndroidManifest.xml
See [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml).

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Colors**: [colors.xml](file:///app/src/main/res/values/colors.xml)
- **Drawables**: [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml), [button_bg.xml](file:///app/src/main/res/drawable/button_bg.xml)

## 9. Build Configuration
See [build.gradle](file:///app/build.gradle).

## 10. Execution Instructions
1. Open `Practical_08_Location_Sensors_Hardware` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or physical device.
4. **Testing Location**:
   - Tap **Get Current Location**. Grant location permission when prompted.
   - On emulator, open emulator extended controls (three dots on toolbar) -> **Location** tab, enter coordinates (e.g. Lat: 37.422, Lng: -122.084), and click **Send**.
5. **Testing Sensor**:
   - On physical device, tilt or shake the phone to observe the \(X, Y, Z\) values change dynamically.
   - On emulator, open emulator extended controls -> **Virtual Sensors** tab, adjust device pose sliders.
6. **Testing Hardware Peripherals**:
   - Tap **Trigger Haptic Vibration** to feel the vibration motor pulse.
   - Tap **Toggle Flashlight** to switch the rear LED torch on/off.

## 11. Expected Output
- **Location Section**: Displays Latitude, Longitude, Altitude, Accuracy (± meters), and Provider (GPS / NETWORK).
- **Sensor Section**: Live streams Accelerometer acceleration in \(\text{m/s}^2\) across \(X\), \(Y\), and \(Z\) axes, updating state to "SHAKE DETECTED!" when moved rapidly.
- **Hardware Section**: Shows current battery percentage and enables interactive haptic feedback and torch toggling.

## 12. Testing
| Test Case ID | Test Scenario | Expected Result | Status |
|---|---|---|---|
| TC_08_01 | Location Permission Prompt | Runtime permission dialog appears requesting location access | Pass |
| TC_08_02 | Location Coordinates Display | Latitude, Longitude, and Provider update upon fix | Pass |
| TC_08_03 | Live Accelerometer Stream | Dynamic \(X, Y, Z\) acceleration updates at ~10Hz | Pass |
| TC_08_04 | Shake Detection | Rapid motion triggers "SHAKE DETECTED!" badge in red | Pass |
| TC_08_05 | Haptic Vibration Pulse | Physical device vibrates for 150ms upon button press | Pass |
| TC_08_06 | Camera Flashlight Toggle | LED flash toggles ON and OFF via `setTorchMode()` | Pass |
| TC_08_07 | Battery State Query | Hardware battery percentage and charging state displayed | Pass |

## 13. Common Errors and Solutions
- **Error**: `SecurityException: "gps" location provider requires ACCESS_FINE_LOCATION permission`.
  - **Solution**: Always request `Manifest.permission.ACCESS_FINE_LOCATION` at runtime before calling `requestLocationUpdates()`.
- **Error**: Sensor values not updating.
  - **Solution**: Ensure `sensorManager.registerListener()` is called inside `onResume()`.
- **Error**: Severe battery drain.
  - **Solution**: Always unregister sensor and location listeners in `onPause()` so hardware is put to sleep when the app is backgrounded.
- **Error**: `CameraAccessException` on emulator when toggling torch.
  - **Solution**: Most desktop emulators lack a virtual camera flash LED. Test flashlight functionality on a physical phone.

## 14. Viva Questions and Answers
1. **Q: What is the difference between `ACCESS_FINE_LOCATION` and `ACCESS_COARSE_LOCATION`?**
   - **A**: `ACCESS_FINE_LOCATION` provides high-precision coordinates using GPS satellites and Wi-Fi networks (accurate within meters). `ACCESS_COARSE_LOCATION` provides approximate city-block-level location derived from cellular tower triangulation (accurate within several hundred meters).
2. **Q: Why must sensor listeners be unregistered in `onPause()`?**
   - **A**: Physical sensors (especially high-frequency motion and gyro sensors) require dedicated hardware power rails. Keeping them active while an Activity is paused rapidly drains the device battery and generates unwanted CPU wake locks.
3. **Q: What are the three axes of the Android Accelerometer?**
   - **A**:
     - \(X\)-axis: Horizontal axis pointing to the right of the screen.
     - \(Y\)-axis: Vertical axis pointing to the top of the screen.
     - \(Z\)-axis: Perpendicular axis pointing out of the front face of the screen toward the user.
4. **Q: How does `CameraManager.setTorchMode()` work without opening the camera preview?**
   - **A**: Introduced in Android 6.0 (API 23), `setTorchMode()` allows direct programmatic control of the camera flash hardware LED without requiring a camera session, texture surface, or camera preview capture request.
5. **Q: What is a sticky broadcast and how is it used for reading battery status?**
   - **A**: A sticky broadcast remains cached in the Android OS after being sent. Calling `registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED))` returns the current sticky battery status immediately without needing an active receiver registered.
