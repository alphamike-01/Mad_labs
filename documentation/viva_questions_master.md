# Mobile Application Development (Java) — Master Viva Voce Question Bank

A comprehensive collection of **60+ viva voce examination questions and in-depth answers** covering all practical topics, Android platform architecture, component lifecycles, and modern security patterns.

---

## Category 1: Android Architecture & Core Fundamentals

### Q1: What are the four core application components in Android?
**Answer**:
1. **Activity**: Represents a single screen with a user interface where users perform actions.
2. **Service**: Runs in the background to perform long-running operations without providing a user interface.
3. **BroadcastReceiver**: Listens for and responds to system-wide or application-specific broadcast announcements.
4. **ContentProvider**: Manages access to a structured repository of data, allowing secure data sharing across applications.

### Q2: What is the Android Manifest file and what is its role?
**Answer**:
`AndroidManifest.xml` is the root configuration file for every Android application. It declares:
- The unique application package and component declarations (Activities, Services, Receivers, Providers).
- Required hardware and software permissions (e.g., `INTERNET`, `READ_CONTACTS`, `POST_NOTIFICATIONS`).
- Application theme, icon, minimum and target SDK versions, and intent filters.

### Q3: What is the Android Activity Lifecycle?
**Answer**:
An Activity transitions through seven fundamental lifecycle callbacks:
1. `onCreate()`: Called when the activity is first created; used to initialize views, inflate XML, and bind data.
2. `onStart()`: Called when the activity becomes visible to the user.
3. `onResume()`: Called when the activity enters the foreground and is ready for user interaction.
4. `onPause()`: Called when the activity partially loses focus (e.g. a dialog appears).
5. `onStop()`: Called when the activity is no longer visible to the user.
6. `onRestart()`: Called after `onStop()` before `onStart()` when the user navigates back to the activity.
7. `onDestroy()`: Called before the activity is destroyed either by `finish()` or by the OS reclaiming memory.

### Q4: What is the difference between `dp`, `sp`, and `px`?
**Answer**:
- `px` (Pixels): Actual physical pixels on screen. Does not scale across different display densities.
- `dp` (Density-Independent Pixels): An abstract unit based on a 160 dpi baseline screen (1 dp = 1 px at 160 dpi). Automatically scales to maintain consistent physical size across different screen densities.
- `sp` (Scale-Independent Pixels): Similar to `dp`, but also scales according to the user's system font size preference in device accessibility settings. Used exclusively for typography.

---

## Category 2: UI Design, Layouts & Menus (Practical 03 & 11)

### Q5: What is the advantage of ConstraintLayout over LinearLayout?
**Answer**:
`ConstraintLayout` enables building complex, responsive layouts with a completely flat view hierarchy. In deeply nested `LinearLayout` hierarchies, the Android rendering engine must perform multiple recursive measuring and layout passes, increasing rendering latency. `ConstraintLayout` solves this by calculating relative constraints simultaneously.

### Q6: How does Android create an Options Menu?
**Answer**:
The Activity overrides `onCreateOptionsMenu(Menu menu)`, obtains a `MenuInflater` via `getMenuInflater()`, and inflates a menu XML resource via `inflater.inflate(R.menu.menu_name, menu)`. User selections are handled in `onOptionsItemSelected(MenuItem item)`.

### Q7: What is the ViewHolder pattern and why is it used in RecyclerView?
**Answer**:
The ViewHolder pattern stores references to the view components of an item layout inside an object. When a user scrolls a list, `RecyclerView` recycles off-screen view holders and rebinds them with new data instead of continually inflating new XML layouts or calling expensive `findViewById()` lookups.

### Q8: How are views created dynamically in Java?
**Answer**:
Views are created dynamically by instantiating their Java classes with a `Context` (e.g., `TextView tv = new TextView(this)`), configuring properties via setters (`tv.setText()`, `tv.setTextColor()`), assigning layout parameters (`new LinearLayout.LayoutParams(width, height)`), and attaching them to a container via `container.addView(tv)`.

---

## Category 3: Intents & Navigation (Practical 04)

### Q9: What is the difference between an Explicit Intent and an Implicit Intent?
**Answer**:
- **Explicit Intent**: Specifies the exact destination component class name (e.g., `new Intent(this, SecondActivity.class)`). Used for navigation within the same application.
- **Implicit Intent**: Specifies an abstract action (e.g., `Intent.ACTION_VIEW`, `Intent.ACTION_DIAL`) and data URI, allowing the Android OS to identify and present all installed applications capable of handling that action.

### Q10: How do you pass data between activities using an Intent?
**Answer**:
Data is passed as key-value pairs inside Intent Extras:
- Sender: `intent.putExtra("KEY_NAME", "Alice"); intent.putExtra("KEY_AGE", 21);`
- Receiver:
  ```java
  Intent intent = getIntent();
  String name = intent.getStringExtra("KEY_NAME");
  int age = intent.getIntExtra("KEY_AGE", 0);
  ```

### Q11: What is the Activity Back Stack?
**Answer**:
The Back Stack is a Last-In, First-Out (LIFO) stack managed by the Android OS where activities are pushed when started and popped when destroyed via the Back button or `finish()`.

---

## Category 4: Concurrency & Background Processing (Practical 05)

### Q12: Why can't long-running computations be performed on the Main Thread?
**Answer**:
The Main Thread handles all UI rendering, touch dispatching, and animation updates. If a task blocks the Main Thread for more than 5 seconds, the OS flags the app as unresponsive and displays the **ANR (Application Not Responding)** dialog.

### Q13: What is the role of `Looper` and `Handler` in Android concurrency?
**Answer**:
- **`Looper`**: A message pump running inside a thread that continually processes messages and runnables queued in its `MessageQueue`.
- **`Handler`**: Allows background threads to post runnable tasks or messages to a specific `Looper`'s queue (such as `Looper.getMainLooper()` for the UI thread).

### Q14: Why was `AsyncTask` deprecated?
**Answer**:
`AsyncTask` had multiple architectural flaws: it frequently leaked Activity contexts on screen rotations, had inconsistent serial vs. parallel thread-pool execution behaviors across Android releases, and lacked structured cancellation handling. Modern Android uses `ExecutorService`, `WorkManager`, or Kotlin Coroutines.

---

## Category 5: Notifications & Security (Practical 06)

### Q15: What is a Notification Channel and why is it mandatory?
**Answer**:
Introduced in Android 8.0 (API 26), Notification Channels group notifications by functional category (e.g. Orders, News). Users have granular control to silence, vibrate, or block individual channels without disabling all notifications from the app. Posting a notification without a registered channel on Android 8.0+ fails silently.

### Q16: What is a PendingIntent and what does `FLAG_IMMUTABLE` do?
**Answer**:
A `PendingIntent` is a token granting an external system service (such as NotificationManager) authorization to execute an Intent on behalf of your application with your app's permissions. `FLAG_IMMUTABLE` guarantees that receiving components cannot mutate or inject malicious intent parameters, preventing privilege escalation exploits.

### Q17: What permission is required to post notifications on Android 13+?
**Answer**:
Android 13 (API 33) introduced the dangerous runtime permission `android.permission.POST_NOTIFICATIONS`. Apps must declare this in the manifest and request runtime approval from the user before posting notifications.

---

## Category 6: Broadcast Receivers (Practical 07)

### Q18: What is the difference between Manifest-declared and Dynamically-registered receivers?
**Answer**:
- **Manifest-declared**: Defined statically in `AndroidManifest.xml`. Can be invoked by the OS even if the app process is not running (heavily restricted in Android 8.0+ for implicit broadcasts).
- **Dynamically-registered**: Registered in Java via `registerReceiver()` during an Activity/Service lifecycle. Only active while the hosting component is running.

### Q19: What is the maximum execution time allowed for `onReceive()`?
**Answer**:
`onReceive()` runs synchronously on the main thread and must complete within **10 seconds**. Heavy work must be offloaded to an `ExecutorService` or `WorkManager`.

### Q20: What does `RECEIVER_NOT_EXPORTED` mean in Android 14?
**Answer**:
`ContextCompat.RECEIVER_NOT_EXPORTED` ensures that only broadcasts originated by your own application can trigger the receiver, blocking external malicious applications from spoofing broadcasts.

---

## Category 7: Services (Practical 09)

### Q21: Does an Android Service run on a separate background thread by default?
**Answer**:
No. A Service runs directly on the application's Main (UI) thread by default. Heavy or blocking tasks inside a Service must spawn their own background worker thread to prevent freezing the UI.

### Q22: What is the difference between `onCreate()` and `onStartCommand()` in a Service?
**Answer**:
`onCreate()` is called only once when the service instance is first created. `onStartCommand()` is called every time a client calls `startService()`, allowing the service to process new requests with incrementing `startId` values.

### Q23: What does returning `START_STICKY` from `onStartCommand()` do?
**Answer**:
If the Android system terminates the service to reclaim memory, `START_STICKY` instructs the OS to recreate the service once resources become available, passing a null intent unless pending start intents exist.

---

## Category 8: Content Providers & Contacts (Practical 10)

### Q24: What is a ContentProvider?
**Answer**:
A ContentProvider encapsulates data storage (often SQLite) and provides an abstraction layer allowing other applications to query, insert, update, and delete records securely via a Content URI.

### Q25: How do you read contacts in Android?
**Answer**:
By querying `ContactsContract.CommonDataKinds.Phone.CONTENT_URI` using `getContentResolver().query()` with projection columns `DISPLAY_NAME` and `NUMBER`, while holding the `READ_CONTACTS` runtime permission.

### Q26: Why must a database `Cursor` be closed?
**Answer**:
A `Cursor` holds native database connections, memory buffers, and file handles. Failing to call `cursor.close()` leads to cursor leaks, memory pressure, and SQLite database locking errors.

---

## Category 9: Networking & REST APIs (Practical 12)

### Q27: What is `NetworkOnMainThreadException`?
**Answer**:
A fatal runtime exception thrown by the Android OS whenever an application attempts to perform a network I/O operation (such as opening an HTTP socket) on the main UI thread.

### Q28: How does `HttpURLConnection` handle HTTP requests?
**Answer**:
It instantiates an HTTP connection to a given `URL`, configures headers and request methods (`GET`, `POST`), manages socket timeouts, executes the request over TLS, and exposes the response byte stream via `getInputStream()`.

### Q29: What is JSON and how is it parsed natively in Android?
**Answer**:
JSON (JavaScript Object Notation) is a text format for structured data. Android parses JSON natively using `org.json.JSONObject` (for key-value maps) and `org.json.JSONArray` (for ordered lists).

---

## Category 10: Canvas & 2D Graphics (Practical 13)

### Q30: What is the role of `Canvas` vs. `Paint`?
**Answer**:
`Canvas` represents the drawing surface providing geometric methods (`drawRect()`, `drawCircle()`, `drawLine()`). `Paint` stores the style attributes (color, stroke width, anti-aliasing, fill style) that dictate how the geometry is rendered.

### Q31: What is the difference between `invalidate()` and `postInvalidate()`?
**Answer**:
`invalidate()` must be called from the main UI thread to request a redraw. `postInvalidate()` is thread-safe and can be called from any background worker thread; it posts a redraw request to the main thread's event queue.

---

## Category 11: Multimedia & Animation (Practical 14)

### Q32: Why must `mediaPlayer.release()` be called in `onDestroy()`?
**Answer**:
`MediaPlayer` controls native hardware audio decoders and system audio resources outside the Java Garbage Collector. Calling `release()` deallocates these native hardware resources immediately.

### Q33: How does `ObjectAnimator` work?
**Answer**:
`ObjectAnimator` animates an object property by repeatedly calling the property's setter method (e.g. `setRotation()`) over a specified duration according to a mathematical time interpolator (e.g. `LinearInterpolator`).
