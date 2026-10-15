# Practical 09: Android Service

## 1. Aim
To create an Android Service, Start and stop it using buttons and observe the lifecycle methods (onStart () and onDestroy () ) in logcat.

## 2. Theory
A **Service** is an application component in Android that can perform long-running operations in the background without providing a user interface.

### Types of Services:
1. **Started Service**: Started when another component (such as an Activity) calls `startService()`. Once started, a service can run in the background indefinitely, even if the initiating component is destroyed. It stops when `stopService()` or `stopSelf()` is called.
2. **Bound Service**: Offers a client-server interface that allows components to bind to the service, send requests, receive results, and perform interprocess communication (IPC). It is created via `bindService()` and stops when all clients unbind.

### Lifecycle of a Started Service:
1. **`onCreate()`**: Invoked once when the service is initially instantiated. Used for one-time initialization.
2. **`onStartCommand(Intent intent, int flags, int startId)`**: Invoked every time a client calls `startService()`. The integer `startId` uniquely identifies each start request. Returns flags such as `START_STICKY`, `START_NOT_STICKY`, or `START_REDELIVER_INTENT`.
3. **`onDestroy()`**: Called when the service is stopped via `stopService()` or `stopSelf()`. All active threads and resources must be released here.

### Android 8.0+ Background Execution Limits:
Starting with Android 8.0 (API 26), background execution limits prohibit background services when the app is in the background. Long-running tasks must either be run while the app is foregrounded, converted to Foreground Services displaying persistent notifications, or scheduled with `WorkManager`.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23

## 4. Algorithm
1. **Declare Service in Manifest**: Add `<service android:name=".MyCustomService" android:exported="false" />` inside `<application>`.
2. **Implement `MyCustomService`**:
   - Extend `android.app.Service`.
   - In `onCreate()`: Log lifecycle entry to Logcat and display Toast.
   - In `onStartCommand()`: Log `startId` and start background heartbeat thread logging every 3 seconds. Return `START_STICKY`.
   - In `onDestroy()`: Set running flag to false, interrupt worker thread, log exit to Logcat, and display Toast.
   - In `onBind()`: Return `null`.
3. **Control from `MainActivity`**:
   - On **Start Service** button tap: Call `startService(new Intent(this, MyCustomService.class))`.
   - On **Stop Service** button tap: Call `stopService(new Intent(this, MyCustomService.class))`.
4. **Logcat Monitoring**: Inspect Logcat output using tag `MyCustomService`.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical09/MainActivity.java)
- [MyCustomService.java](file:///app/src/main/java/com/example/practical09/MyCustomService.java)

## 6. XML Layouts
- [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml)

## 7. AndroidManifest.xml
- [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml)

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml), [button_bg.xml](file:///app/src/main/res/drawable/button_bg.xml)

## 9. Build Configuration
- [build.gradle](file:///app/build.gradle)

## 10. Execution Instructions
1. Open `Practical_09_Android_Service` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or device.
4. Open the Logcat window and enter the filter: `tag:MyCustomService`.
5. Tap **Start Service** -> Check Logcat.
6. Tap **Start Service** again -> Check Logcat for subsequent `onStartCommand()` calls with new `startId`.
7. Tap **Stop Service** -> Check Logcat for `onDestroy()`.

## 11. Expected Output
- **Start Service**:
  - Toast: "Service: onCreate() called"
  - Toast: "Service: onStartCommand() [ID: 1]"
  - Logcat:
    ```text
    [LIFECYCLE] onCreate() invoked! Service instance created.
    [LIFECYCLE] onStartCommand() invoked! startId = 1
    [RUNNING] Background task heartbeat #1
    [RUNNING] Background task heartbeat #2
    ```
- **Stop Service**:
  - Toast: "Service: onDestroy() called"
  - Logcat:
    ```text
    [LIFECYCLE] onDestroy() invoked! Service terminated.
    ```

## 12. Testing
| Test Case ID | Test Action | Expected Result | Status |
|---|---|---|---|
| TC_09_01 | Tap Start Service once | `onCreate()` called, then `onStartCommand()` with `startId=1` | Pass |
| TC_09_02 | Tap Start Service multiple times | `onCreate()` not repeated; `onStartCommand()` called with incrementing `startId` (2, 3...) | Pass |
| TC_09_03 | Tap Stop Service | `onDestroy()` invoked; background heartbeat thread terminates cleanly | Pass |
| TC_09_04 | Repeated Stop Service | No crash; handled gracefully | Pass |

## 13. Common Errors and Solutions
- **Error**: `ClassNotFoundException` or Service not starting.
  - **Solution**: Ensure `<service android:name=".MyCustomService" />` is declared inside `AndroidManifest.xml`.
- **Error**: UI thread freezes during service execution.
  - **Solution**: Services run on the main UI thread by default! Long computations inside a service must spawn their own worker thread.
- **Error**: Service destroyed immediately on app backgrounding (Android 8.0+).
  - **Solution**: For true background services, migrate to `WorkManager` or start a Foreground Service with `startForeground()`.

## 14. Viva Questions and Answers
1. **Q: Does a Service run on its own separate thread by default?**
   - **A**: No! By default, a Service runs directly on the Main (UI) thread of the application. Heavy or blocking operations executed inside a service must create a background thread to avoid ANR.
2. **Q: What is the difference between `onCreate()` and `onStartCommand()`?**
   - **A**: `onCreate()` is invoked only once when the service is created. `onStartCommand()` is invoked every single time `startService()` is called, even if the service is already running.
3. **Q: What does returning `START_STICKY` from `onStartCommand()` signify?**
   - **A**: If the Android system terminates the service due to low memory, `START_STICKY` instructs the OS to recreate the service once resources become available, passing a null intent.
4. **Q: How does a Bound Service differ from a Started Service?**
   - **A**: A Started Service runs independently until explicitly stopped. A Bound Service lives only as long as another component is bound to it; when all clients unbind, the service is destroyed automatically.
5. **Q: Why was `JobIntentService` and `WorkManager` introduced to replace background Services?**
   - **A**: To conserve battery life and system memory, modern Android versions restrict background services. `WorkManager` intelligently defers and batches background jobs based on system constraints (e.g. device charging, connected to Wi-Fi).
