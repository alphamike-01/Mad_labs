# Android Studio Practical Troubleshooting Guide

This guide details diagnostics and solutions for the most frequent issues encountered in university computer science and engineering laboratories.

---

### 1. Build & Gradle Sync Issues

#### Problem: "Could not resolve com.android.tools.build:gradle:8.5.2"
- **Cause**: Computer is offline or cannot connect to Google's Maven repository due to laboratory firewall/proxy settings.
- **Solution**:
  1. Open **File -> Settings -> Appearance & Behavior -> System Settings -> HTTP Proxy**.
  2. Enter your institution's proxy host and port, then click **Check connection**.
  3. Alternatively, connect your computer to a mobile Wi-Fi hotspot to allow Gradle to download required dependencies once. After initial download, dependencies are cached locally in `~/.gradle/caches`.

#### Problem: "Unsupported class file major version / Incompatible Java version"
- **Cause**: Project was opened using an outdated JDK (e.g. JDK 8 or 11), but Android Gradle Plugin 8.x requires JDK 17 or higher.
- **Solution**: Go to **File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle**, and change **Gradle JDK** to **Embedded JDK 17 / 21**.

---

### 2. Runtime & Device Issues

#### Problem: "Application Not Responding (ANR)"
- **Cause**: Heavy mathematical computations, file reading, or network requests were executed directly on the Main (UI) thread.
- **Solution**: Inspect your code to ensure all long operations are submitted to an `ExecutorService` and results dispatched back via `Handler(Looper.getMainLooper()).post()`.

#### Problem: `CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.`
- **Cause**: Attempting to update a `TextView`, `Button`, or `ProgressBar` from inside a background worker thread.
- **Solution**: Wrap the UI update inside:
  ```java
  new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
          textView.setText("Updated from background");
      }
  });
  ```

#### Problem: `SecurityException: Permission Denial`
- **Cause**: Trying to access protected resources (Contacts, Notifications) without declaring permissions in `AndroidManifest.xml` or without requesting runtime authorization on Android 6.0+ / Android 13+.
- **Solution**:
  1. Verify the permission exists in `AndroidManifest.xml` (e.g. `<uses-permission android:name="android.permission.READ_CONTACTS" />`).
  2. Implement `ContextCompat.checkSelfPermission()` and `ActivityCompat.requestPermissions()`.

#### Problem: Empty contacts list on emulator in Practical 10
- **Cause**: Freshly created Android Virtual Devices have an empty contact book.
- **Solution**: Open the **Contacts** app inside the running emulator, tap **+ (Create Contact)**, and add 2-3 sample names and phone numbers. Re-run Practical 10.

#### Problem: Audio not playing in Practical 14
- **Cause**: Computer volume is muted or emulator sound output is disabled.
- **Solution**: Check your host computer sound output and verify emulator volume using the physical volume rocker on the emulator side toolbar.
