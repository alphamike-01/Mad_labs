# Practical 05: Prime Number Checking with ExecutorService

## 1. Aim
To perform a background computation (Prime Number checking) using Executor Service and update after the task is completed.

## 2. Theory
Android enforces a strict single-thread model where all user interface events, drawing, and user interactions take place exclusively on the **Main Thread** (often referred to as the UI Thread).

### Application Not Responding (ANR):
If an application executes an intensive computation, file I/O, or network transaction on the Main Thread for more than 5 seconds, the Android OS interrupts the app with an **Application Not Responding (ANR)** dialog, offering the user the option to force-close it.

### Java Concurrency & ExecutorService:
The `java.util.concurrent.ExecutorService` provides a standardized asynchronous task execution framework:
- **`Executors.newSingleThreadExecutor()`**: Creates an executor that manages a single background worker thread, queuing tasks sequentially.
- **Worker Thread Execution**: Heavy mathematical calculations run in isolation on the background thread without freezing user input or UI animations.
- **`Handler(Looper.getMainLooper())`**: Because background threads cannot directly modify UI widgets (which triggers a `CalledFromWrongThreadException`), a `Handler` attached to the main application `Looper` delivers the results back to the UI thread via `handler.post(Runnable)`.

### Primality Algorithm:
A number \(n > 1\) is prime if it is divisible only by 1 and \(n\). Optimizations:
1. Numbers \(\le 1\) are not prime.
2. 2 is prime; all other even numbers are composite.
3. For odd numbers, check odd divisors up to \(\lfloor\sqrt{n}\rfloor\).

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23

## 4. Algorithm
1. **Initialize Concurrency Tools**: Create an `ExecutorService` via `Executors.newSingleThreadExecutor()` and a `Handler` with `Looper.getMainLooper()`.
2. **Collect Input**: Read input string from `etNumber`.
3. **Validate Input**: Check for empty string, invalid characters, negative numbers, or overflow using `Long.parseLong()`.
4. **Update UI State**: Display `ProgressBar` and disable the Check button to prevent duplicate submissions.
5. **Dispatch Task**: Call `executorService.execute(Runnable)` to run primality checking on the background thread.
6. **Execute Primality Test**:
   - Check if \(n \le 1\) (false).
   - Check if \(n = 2\) (true).
   - Check if \(n \pmod 2 = 0\) (false).
   - Loop \(i = 3\) to \(\sqrt{n}\) with step 2. If \(n \pmod i = 0\), mark composite.
7. **Post to Main Thread**: Use `mainHandler.post()` to hide `ProgressBar`, re-enable button, and display result summary.
8. **Resource Cleanup**: Inside `onDestroy()`, invoke `executorService.shutdownNow()`.

## 5. Complete Source Code
See [MainActivity.java](file:///app/src/main/java/com/example/practical05/MainActivity.java).

## 6. XML Layouts
See [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml).

## 7. AndroidManifest.xml
See [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml).

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml), [button_bg.xml](file:///app/src/main/res/drawable/button_bg.xml)

## 9. Build Configuration
See [build.gradle](file:///app/build.gradle).

## 10. Execution Instructions
1. Open `Practical_05_Prime_Number_ExecutorService` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or device.
4. Input test numbers such as `29`, `104729`, or `1000000007` and press **Check Prime in Background**.

## 11. Expected Output
- Progress bar spins while computation executes in the background.
- UI remains responsive (no freezing).
- The result card updates with the primality verdict, divisor explanation, and computation time in milliseconds.

## 12. Testing
| Test Case ID | Test Number | Expected Output | Status |
|---|---|---|---|
| TC_05_01 | `2` | PRIME (Smallest prime) | Pass |
| TC_05_02 | `1` | NOT A PRIME (<= 1) | Pass |
| TC_05_03 | `0` / `-7` | NOT A PRIME (<= 1) | Pass |
| TC_05_04 | `9` | NOT A PRIME (Divisible by 3) | Pass |
| TC_05_05 | `104729` | PRIME NUMBER | Pass |
| TC_05_06 | `1000000007` | PRIME NUMBER (Large integer) | Pass |

## 13. Common Errors and Solutions
- **Error**: `CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.`
  - **Solution**: Always post UI updates through `Handler(Looper.getMainLooper()).post()` or `runOnUiThread()`.
- **Error**: Memory leaks when exiting the activity during background calculation.
  - **Solution**: Call `executorService.shutdownNow()` in `onDestroy()`.
- **Error**: NumberFormatException with extremely large input.
  - **Solution**: Catch `NumberFormatException` and set input validation error on the `EditText`.

## 14. Viva Questions and Answers
1. **Q: Why can't we update UI elements from a background thread in Android?**
   - **A**: The Android UI toolkit is not thread-safe. Allowing concurrent modifications from multiple threads would cause race conditions, rendering corruption, and unpredictable state crashes.
2. **Q: What is an ANR error and how is it triggered?**
   - **A**: ANR stands for Application Not Responding. It is triggered by the Android system when an app blocks the main UI thread for longer than 5 seconds without processing user input events.
3. **Q: What is the purpose of `Looper.getMainLooper()`?**
   - **A**: `Looper.getMainLooper()` returns the message queue looper associated with the application's main (UI) thread, enabling handlers to dispatch runnables directly to the UI thread.
4. **Q: Why was `AsyncTask` deprecated in Android?**
   - **A**: `AsyncTask` frequently caused memory leaks when retaining references to destroyed activities, failed to handle configuration changes (like screen rotation), and had confusing concurrency behavior across different Android versions. `ExecutorService` and Coroutines are modern replacements.
5. **Q: What is the difference between `executorService.shutdown()` and `executorService.shutdownNow()`?**
   - **A**: `shutdown()` initiates an orderly shutdown allowing previously submitted tasks to complete before termination, whereas `shutdownNow()` attempts to stop all actively executing tasks immediately and returns queued tasks.
