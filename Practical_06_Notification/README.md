# Practical 06: Android Notifications

## 1. Aim
To create a simple Android Notification with a title, body and small icon and implement a top action that opens another activity when the notification is clicked.

## 2. Theory
An **Android Notification** is a message displayed outside your application's normal user interface to provide the user with reminders, alerts, or status information from communication services.

### Core Concepts:
1. **NotificationManager**: The system service responsible for issuing, updating, and cancelling notifications (`context.getSystemService(NotificationManager.class)`).
2. **NotificationChannel (Android 8.0+, API 26+)**: Categorizes notifications so users can customize notifications per category (channel) rather than enabling or disabling all notifications for the app.
3. **NotificationCompat.Builder**: Backward-compatible builder providing a fluent API to define:
   - **Small Icon**: Monochromatic vector icon displayed in the status bar (`setSmallIcon()`).
   - **Title**: Primary notification header (`setContentTitle()`).
   - **Body**: Detailed description text (`setContentText()` or `setStyle(BigTextStyle)`).
   - **AutoCancel**: Automatically dismisses the notification when the user taps it (`setAutoCancel(true)`).
4. **PendingIntent**: A token granted to an external system process (the Android Notification Manager) allowing it to execute an intent on behalf of your application with your application's permissions.
5. **Runtime Permission (Android 13+, API 33+)**: Apps targeting API 33+ must request `android.permission.POST_NOTIFICATIONS` at runtime.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23

## 4. Algorithm
1. **Declare Permissions**: In `AndroidManifest.xml`, declare `<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />`.
2. **Declare Activities**: Register `MainActivity` and `NotificationDetailActivity` in the manifest.
3. **Create Notification Channel**: In `MainActivity.onCreate()`, if device API \(\ge 26\), create a `NotificationChannel` with ID `"practical_06_channel"` and register it with `NotificationManager`.
4. **Request Runtime Permission**: On Android 13+ (API 33+), verify if `POST_NOTIFICATIONS` is granted. If not, invoke `ActivityCompat.requestPermissions()`.
5. **Construct PendingIntent**:
   - Create explicit intent `new Intent(this, NotificationDetailActivity.class)`.
   - Wrap in `PendingIntent.getActivity()` with flag `FLAG_IMMUTABLE`.
6. **Build Notification**: Instantiate `NotificationCompat.Builder` with channel ID, title, text, small icon, content intent, and `setAutoCancel(true)`.
7. **Post Notification**: Call `notificationManager.notify(NOTIFICATION_ID, builder.build())`.
8. **Handle Notification Click**: When tapped, the system launches `NotificationDetailActivity`, displaying the transmitted payload.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical06/MainActivity.java)
- [NotificationDetailActivity.java](file:///app/src/main/java/com/example/practical06/NotificationDetailActivity.java)

## 6. XML Layouts
- [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml)
- [activity_notification_detail.xml](file:///app/src/main/res/layout/activity_notification_detail.xml)

## 7. AndroidManifest.xml
- [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml)

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [ic_notification.xml](file:///app/src/main/res/drawable/ic_notification.xml)

## 9. Build Configuration
- [build.gradle](file:///app/build.gradle)

## 10. Execution Instructions
1. Open `Practical_06_Notification` in Android Studio.
2. Synchronize Gradle project.
3. Run on an Android emulator (API 30, 33, or 34 recommended) or physical device.
4. If running on Android 13+, grant notification permission when prompted.
5. Tap **Trigger Notification**. Pull down the notification shade and tap the notification to open `NotificationDetailActivity`.

## 11. Expected Output
- Status bar displays the small notification bell icon.
- Expanding the notification drawer displays title "MAD Practical 06: New Alert" and description.
- Tapping the notification dismisses it and immediately opens `NotificationDetailActivity` displaying timestamp payload details.

## 12. Testing
| Test Case ID | Test Scenario | Expected Result | Status |
|---|---|---|---|
| TC_06_01 | Android 13+ First Run | Runtime permission prompt appears requesting notification access | Pass |
| TC_06_02 | Permission Granted | Notification posted in status bar | Pass |
| TC_06_03 | Permission Denied | Toast message explains permission requirement; no crash | Pass |
| TC_06_04 | Notification Click | `NotificationDetailActivity` launches with payload | Pass |
| TC_06_05 | AutoCancel Test | Notification is removed from drawer upon being clicked | Pass |

## 13. Common Errors and Solutions
- **Error**: `IllegalArgumentException: Targeting S+ (version 31 and above) requires that one of FLAG_IMMUTABLE or FLAG_MUTABLE be specified.`
  - **Solution**: Always specify `PendingIntent.FLAG_IMMUTABLE` (or `FLAG_MUTABLE`) when constructing a `PendingIntent`.
- **Error**: Notifications not appearing on Android 8.0+.
  - **Solution**: Ensure `NotificationChannel` is created and registered before calling `notify()`.
- **Error**: Notifications not appearing on Android 13+.
  - **Solution**: Ensure `POST_NOTIFICATIONS` permission is declared in manifest and granted at runtime.

## 14. Viva Questions and Answers
1. **Q: What is a Notification Channel and why was it introduced?**
   - **A**: Introduced in Android 8.0 (API 26), Notification Channels allow developers to categorize notifications into distinct channels (e.g. Alerts, Promotions). Users can individually configure vibration, sound, or completely silence specific channels without blocking all notifications from the app.
2. **Q: What is a PendingIntent?**
   - **A**: A `PendingIntent` is a security wrapper around an `Intent` that grants another application or system process (like NotificationManager or AlarmManager) the authorization to execute the intent with the identity and permissions of the creating application.
3. **Q: Why is `FLAG_IMMUTABLE` required in modern Android versions?**
   - **A**: `FLAG_IMMUTABLE` guarantees that external receiving processes cannot alter or hijack the underlying Intent's parameters or target component, preventing privilege escalation vulnerabilities.
4. **Q: What is the purpose of `setAutoCancel(true)`?**
   - **A**: `setAutoCancel(true)` automatically cancels and dismisses the notification from the system status bar as soon as the user taps on it.
5. **Q: What happens if an app posts a notification without a Notification Channel on Android 8.0+?**
   - **A**: The system silently ignores and drops the notification, logging a warning in Logcat, and no notification appears on the user's screen.
