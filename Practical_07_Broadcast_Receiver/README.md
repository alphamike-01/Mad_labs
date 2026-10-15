# Practical 07: Custom BroadcastReceiver

## 1. Aim
TO create a custom Broadcast receiver in Android that listens for custom broadcast and the receiver displays a Toast message and writes a logcat message when the broadcast is received.

## 2. Theory
A **BroadcastReceiver** is an Android application component that allows an app to register for system-wide or application-specific broadcast announcements.

### Architectural Concepts:
1. **Publish-Subscribe Pattern**: Broadcasts implement the publish-subscribe messaging paradigm. Components publish events (`sendBroadcast(intent)`), and interested receivers subscribing to matching intent filters consume them.
2. **Custom Broadcast Action**: A uniquely namespaced string (e.g. `com.example.practical07.CUSTOM_BROADCAST`) defining the custom event contract.
3. **Manifest-Declared vs. Context-Registered (Dynamic) Receivers**:
   - **Manifest-Declared**: Declared in `<receiver>` tag in `AndroidManifest.xml`. Beginning in Android 8.0 (API 26), implicit broadcasts declared in the manifest are heavily restricted to save battery and RAM.
   - **Dynamic (Context-Registered)**: Registered programmatically via `context.registerReceiver()` while an Activity or Service is running. In Android 14 (API 34+), apps must specify `RECEIVER_EXPORTED` or `RECEIVER_NOT_EXPORTED` when registering dynamically.
4. **Explicit Broadcasts**: Setting the package via `intent.setPackage(getPackageName())` converts an implicit broadcast into an explicit broadcast, restricting delivery exclusively to components within your application.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23

## 4. Algorithm
1. **Define Receiver Class**: Create `CustomBroadcastReceiver` extending `BroadcastReceiver`.
2. **Override `onReceive()`**: Inside `onReceive()`, verify the intent action matches `ACTION_CUSTOM_BROADCAST`. Retrieve the payload string.
3. **Logcat & Toast**: Write logging statements using `Log.d("CustomBroadcastReceiver", message)` and trigger `Toast.makeText(context, ...)`.
4. **Register Dynamically**: In `MainActivity`, instantiate `IntentFilter` with `ACTION_CUSTOM_BROADCAST`. Register using `ContextCompat.registerReceiver(..., RECEIVER_NOT_EXPORTED)`.
5. **Construct & Send Broadcast**: On button tap, create `new Intent(ACTION_CUSTOM_BROADCAST)`, attach `EXTRA_MESSAGE`, set package name, and call `sendBroadcast(intent)`.
6. **Unregister on Destroy**: In `onDestroy()`, ensure `unregisterReceiver(receiver)` is invoked to prevent memory leaks.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical07/MainActivity.java)
- [CustomBroadcastReceiver.java](file:///app/src/main/java/com/example/practical07/CustomBroadcastReceiver.java)

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
1. Open `Practical_07_Broadcast_Receiver` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or device.
4. Open the Logcat tool window (**View -> Tool Windows -> Logcat** or Alt + 6) and set the search filter to `tag:CustomBroadcastReceiver`.
5. Click **Send Custom Broadcast**.

## 11. Expected Output
- A Toast notification displays "Broadcast Received: Hello from MAD Practical 07!".
- Logcat prints:
  ```text
  Custom Broadcast Successfully Received!
  Action: com.example.practical07.CUSTOM_BROADCAST
  Message: Hello from MAD Practical 07!
  ```
- Clicking **Unregister Receiver** followed by **Send Custom Broadcast** produces no Toast and no Logcat output, proving dynamic receiver control.

## 12. Testing
| Test Case ID | Test Scenario | Action | Expected Result | Status |
|---|---|---|---|---|
| TC_07_01 | Send with Registered Receiver | Tap Send Broadcast | Toast shown; Logcat entry generated | Pass |
| TC_07_02 | Custom Text Payload | Enter "Test MSG 123" -> Send | Toast & Logcat show "Test MSG 123" | Pass |
| TC_07_03 | Unregister and Send | Tap Unregister -> Send | No Toast; no Logcat entry | Pass |
| TC_07_04 | Re-register and Send | Tap Register -> Send | Receiver active again; Toast & Logcat printed | Pass |

## 13. Common Errors and Solutions
- **Error**: `SecurityException: com.example.practical07: One of RECEIVER_EXPORTED or RECEIVER_NOT_EXPORTED should be specified.`
  - **Solution**: On Android 14+ (API 34+), always use `ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)`.
- **Error**: Receiver not receiving broadcast on Android 8.0+.
  - **Solution**: Modern Android drops implicit background broadcasts. Call `intent.setPackage(getPackageName())` to make it explicit.
- **Error**: `IllegalArgumentException: Receiver not registered`.
  - **Solution**: Guard `unregisterReceiver()` with a boolean flag `isReceiverRegistered` to prevent double-unregistering.

## 14. Viva Questions and Answers
1. **Q: What is the main purpose of a BroadcastReceiver?**
   - **A**: A BroadcastReceiver acts as a gateway to receive system events (e.g. battery low, screen on) or custom inter-process/intra-app notifications, initiating appropriate background handling or UI updates.
2. **Q: What is the difference between Manifest and Dynamic BroadcastReceivers?**
   - **A**: Manifest receivers are registered statically in `AndroidManifest.xml` and can be instantiated by the system even if the app process is not running. Dynamic receivers are registered programmatically in code and only receive events while their hosting lifecycle (Activity/Service) is active.
3. **Q: How long can a BroadcastReceiver run in `onReceive()`?**
   - **A**: The `onReceive()` method executes synchronously on the main thread and must complete within 10 seconds. Lengthy tasks must be offloaded to an `ExecutorService`, `WorkManager`, or `JobScheduler`.
4. **Q: What is an Explicit Broadcast?**
   - **A**: An explicit broadcast sets a specific target package or component using `intent.setPackage()` or `intent.setComponent()`, ensuring the intent is delivered exclusively to that designated receiver.
5. **Q: Why does Android 14 mandate `RECEIVER_NOT_EXPORTED`?**
   - **A**: It prevents other unauthorized malicious third-party apps installed on the same device from sending broadcasts to your private receiver.
