# Mobile Application Development (Java) — Complete Practical Projects

A comprehensive, production-ready, academically rigorous collection of **12 native Android Studio practical projects** developed in **pure Java** for college laboratory curriculums, practical examinations, and viva voce assessments.

---

## 📱 Curricular Practicals Master Index

Each practical is packaged as an independent, fully self-contained Android Studio project with its own Gradle build configuration, manifest, resources, Java logic, and comprehensive documentation:

| Practical # | Official Practical Aim | Project Folder | Primary Android Concepts |
|:---:|---|---|---|
| **03** | To design a simple user interface using linear layout / constraints layout, textview, editview, button and imageview. add drawable resources and an options menu with a settings item. | `Practical_03_UI_Design` | ConstraintLayout, View Binding, Vector Drawables, State-List Selectors, Options Menu, AlertDialog |
| **04** | to implement navigation between two android activities using explicit intent, add an options menu for switching between activities and pass date using intent extras (string and integer) | `Practical_04_Explicit_Intent` | Explicit Intents, Activity Back Stack, Intent Extras (`putExtra`, `getStringExtra`, `getIntExtra`), Options Menu navigation |
| **05** | To perform a background computation (Prime Number checking) using Executor Service and update after the task is completed. | `Practical_05_Prime_Number_ExecutorService` | Java Concurrency, `ExecutorService`, `Handler(Looper.getMainLooper())`, non-blocking UI, mathematical primality algorithms |
| **06** | To create a simple Android Notification with a title, body and small icon and implement a top action that opens another activity when the notification is clicked. | `Practical_06_Notification` | `NotificationManager`, `NotificationChannel` (API 26+), `NotificationCompat.Builder`, `PendingIntent` (`FLAG_IMMUTABLE`), Android 13+ `POST_NOTIFICATIONS` runtime permission |
| **07** | TO create a custom Broadcast receiver in Android that listens for custom broadcast and the receiver displays a Toast message and writes a logcat message when the broadcast is received. | `Practical_07_Broadcast_Receiver` | `BroadcastReceiver`, custom intent actions, dynamic receiver registration (`RECEIVER_NOT_EXPORTED`), explicit broadcasts, Toast, Logcat inspection |
| **08** | location awareness/ sensor + native hardwar access | `Practical_08_Location_Sensors_Hardware` | `LocationManager`, `LocationListener`, `SensorManager` (Accelerometer \(X, Y, Z\)), `Vibrator`, `CameraManager` Flashlight, `BatteryManager` |
| **09** | To create an Android Service, Start and stop it using buttons and observe the lifecycle methods (onStart () and onDestroy () ) in logcat. | `Practical_09_Android_Service` | Android Started Services, lifecycle methods (`onCreate`, `onStartCommand`, `onDestroy`), background worker threads, Logcat monitoring |
| **10** | To create contacts from phone: Develop an application that fetches all contacts from phonebook and displays on screen. | `Practical_10_Phone_Contacts` | `ContentResolver`, `ContactsContract.CommonDataKinds.Phone`, `Cursor` query management, `READ_CONTACTS` runtime permission, `RecyclerView` & ViewHolder |
| **11** | Develop an application that accepts a number from user and then dynamically generates accepted number of items in another activity. | `Practical_11_Dynamic_Views` | Dynamic view instantiation in Java, programmatic `ViewGroup.LayoutParams`, `ScrollView`, dp-to-pixel scaling, memory safety bounds |
| **12** | enterprise data access via internet (REST API) | `Practical_12_REST_API` | Internet permissions, `HttpURLConnection`, `ExecutorService` background networking, JSON parsing (`org.json.JSONObject`), UI state handling, offline tolerance |
| **13** | Graphics and custom views + canvas | `Practical_13_Custom_Graphics_Canvas` | Custom `View` subclassing, 2D `Canvas` drawing primitives (rectangles, circles, lines, arcs, text), `Paint` styling & anti-aliasing, touch events, `invalidate()` |
| **14** | Animation + Multimedia (Audio Playback) | `Practical_14_Animation_Audio` | `MediaPlayer` state machine, bundled raw audio (`res/raw/sample_chime.wav`), property animations (`ObjectAnimator`), continuous 360-degree rotation |

> [!NOTE]
> Practicals 1 and 2 were missing from the source curriculum specifications (`aims.txt`). In accordance with examination guidelines, they have not been arbitrarily invented or renumbered. See [MISSING_AIMS.md](MISSING_AIMS.md) for details.

---

## 🛠 Technology Stack & Requirements

- **Programming Language**: Java only (Java 8 / 11 / 17 / 21 compatible)
- **Development Tool**: Android Studio (Ladybug, Koala, Meerkat, Hedgehog or later)
- **Platform**: Native Android SDK (Min SDK: 23 [Android 6.0], Compile & Target SDK: 34 / 35 [Android 14 / 15])
- **Build System**: Gradle 8.9 with Android Gradle Plugin (AGP) 8.5.2 (Groovy DSL)
- **UI Architecture**: Native Android XML layouts (`ConstraintLayout`, `LinearLayout`, `ScrollView`, `RecyclerView`)
- **Concurrency**: `java.util.concurrent.ExecutorService` and `android.os.Handler`
- **Networking**: Native Java `HttpURLConnection` and `org.json`
- **Media & Hardware**: Native Android `MediaPlayer`, `ObjectAnimator`, `LocationManager`, `SensorManager`, `CameraManager`, `Vibrator`

---

## 🚀 Quick Start Guide

### How to Open an Individual Practical in Android Studio:
1. Launch **Android Studio**.
2. Click **File -> Open...** (or choose **Open** on the welcome screen).
3. Navigate into this extracted directory and select any individual practical folder (e.g. `Practical_08_Location_Sensors_Hardware`).
4. Click **OK**. Android Studio will detect the project's `settings.gradle` and automatically begin the Gradle sync.
5. Once synchronization finishes, select an emulator or connect a physical phone via USB debugging.
6. Click the green **Run** button (or press `Shift + F10`).

---

## 📁 Repository Structure

```text
Mobile_Application_Development_Java/
├── README.md                                  # Root repository overview
├── PRACTICAL_INDEX.md                         # Detailed matrix of all practicals
├── SETUP_GUIDE.md                             # Step-by-step IDE setup & Gradle troubleshooting
├── MISSING_AIMS.md                            # Documentation on unsupplied practicals 1 and 2
├── TESTING_REPORT.md                          # Verification, compilation & runtime audit report
│
├── Practical_03_UI_Design/                   # Practical 3: UI Design with Constraints & Menus
├── Practical_04_Explicit_Intent/              # Practical 4: Explicit Intent Data Navigation
├── Practical_05_Prime_Number_ExecutorService/ # Practical 5: Background Primality with ExecutorService
├── Practical_06_Notification/                 # Practical 6: Channels, Notifications & PendingIntent
├── Practical_07_Broadcast_Receiver/           # Practical 7: Custom BroadcastReceiver & Logcat
├── Practical_08_Location_Sensors_Hardware/    # Practical 8: GPS Location, Accelerometer & Hardware
├── Practical_09_Android_Service/              # Practical 9: Started Service Lifecycle & Logcat
├── Practical_10_Phone_Contacts/               # Practical 10: ContentResolver Phone Contacts Query
├── Practical_11_Dynamic_Views/                # Practical 11: Programmatic Dynamic View Generation
├── Practical_12_REST_API/                     # Practical 12: Enterprise REST API & JSON Client
├── Practical_13_Custom_Graphics_Canvas/       # Practical 13: 2D Canvas & Custom View Subclass
├── Practical_14_Animation_Audio/              # Practical 14: ObjectAnimator & MediaPlayer Audio
│
├── assets/                                    # Shared audio samples & Gradle wrapper templates
└── documentation/                             # Comprehensive reference materials
    ├── practical_summaries.md                 # Educational breakdowns of each project
    ├── troubleshooting_guide.md               # Diagnostic guide for common lab & IDE errors
    └── viva_questions_master.md               # 60+ in-depth viva voce questions & answers
```
