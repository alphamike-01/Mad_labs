# Practical 04: Explicit Intent Navigation

## 1. Aim
to implement navigation between two android activities using explicit intent, add an options menu for switching between activities and pass date using intent extras (string and integer)

## 2. Theory
An **Intent** in Android is an asynchronous messaging object that represents an intention to perform an action. Intents are the primary mechanism for binding separate components (such as Activities, Services, and BroadcastReceivers) at runtime.

### Types of Intents:
1. **Explicit Intent**: Explicitly designates the exact component to be invoked by specifying its fully qualified Java class name (`new Intent(Context, TargetClass.class)`). Explicit intents are used for intra-application communication where the developer controls the destination activity.
2. **Implicit Intent**: Specifies an abstract action to perform (e.g., `ACTION_VIEW`, `ACTION_DIAL`), delegating to the Android runtime to identify suitable applications that declare matching `<intent-filter>` tags in their manifests.

### Passing Data via Intent Extras:
The `Intent` object contains a key-value data carrier known as an `extras` bundle.
- **Storing Data**:
  - `intent.putExtra("KEY_STRING", "Value")`
  - `intent.putExtra("KEY_INT", 101)`
- **Retrieving Data**:
  - `String name = getIntent().getStringExtra("KEY_STRING");`
  - `int roll = getIntent().getIntExtra("KEY_INT", defaultValue);`

### Activity Back Stack:
Activities in Android are managed in a Last-In, First-Out (LIFO) back stack. When `MainActivity` invokes `startActivity()`, `SecondActivity` is pushed onto the stack. Calling `finish()` on `SecondActivity` pops it off, resuming `MainActivity`.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23

## 4. Algorithm
1. **Define Activities**: Create `MainActivity` and `SecondActivity`, declaring both inside `AndroidManifest.xml`.
2. **Setup Sender UI (`MainActivity`)**: Provide input fields for student name and roll number, and a "Send Data" button.
3. **Capture Inputs**: On button click, validate the `EditText` values to ensure non-empty inputs and valid numeric formats.
4. **Construct Explicit Intent**: Instantiate `Intent intent = new Intent(MainActivity.this, SecondActivity.class);`.
5. **Attach Extras**: Call `intent.putExtra(EXTRA_NAME, name)` and `intent.putExtra(EXTRA_ROLL, rollNumber)`.
6. **Launch Activity**: Call `startActivity(intent)`.
7. **Options Menu Navigation**: Provide an options menu in `MainActivity` allowing direct switching between activities.
8. **Receive Extras in Receiver (`SecondActivity`)**: Inside `onCreate()`, invoke `getIntent()` and retrieve `getStringExtra()` and `getIntExtra()`.
9. **Display Transmitted Data**: Render the values in `tvReceivedName` and `tvReceivedRoll`.
10. **Return Mechanism**: Handle back navigation via a button or options menu that invokes `finish()`.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical04/MainActivity.java)
- [SecondActivity.java](file:///app/src/main/java/com/example/practical04/SecondActivity.java)

## 6. XML Layouts
- [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml)
- [activity_second.xml](file:///app/src/main/res/layout/activity_second.xml)

## 7. AndroidManifest.xml
- [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml)

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Menus**: [menu_main.xml](file:///app/src/main/res/menu/menu_main.xml), [menu_second.xml](file:///app/src/main/res/menu/menu_second.xml)

## 9. Build Configuration
- [build.gradle](file:///app/build.gradle)

## 10. Execution Instructions
1. Open `Practical_04_Explicit_Intent` in Android Studio.
2. Synchronize Gradle project.
3. Run on an emulator or connected USB device.
4. Enter Name and Roll number, then tap "Send Data to Second Activity".
5. Alternatively, tap the top-right menu and choose "Switch to Second Activity".

## 11. Expected Output
- `MainActivity` opens with input fields.
- Tapping the button navigates to `SecondActivity`, displaying the exact String name and Integer roll number entered.
- Pressing "Back to Main Activity" closes `SecondActivity` and restores `MainActivity`.

## 12. Testing
| Test Case ID | Test Scenario | Input Data | Expected Result | Status |
|---|---|---|---|---|
| TC_04_01 | Valid Navigation | Name: "Alice", Roll: 42 | SecondActivity opens; displays "Alice" and "42" | Pass |
| TC_04_02 | Empty Name | Name: "", Roll: 42 | Toast shown; navigation halted | Pass |
| TC_04_03 | Empty Roll | Name: "Bob", Roll: "" | Error displayed on Roll field; navigation halted | Pass |
| TC_04_04 | Non-numeric Roll | Name: "Bob", Roll: "XYZ" | Format error handled gracefully without crash | Pass |
| TC_04_05 | Options Menu Switch | Tap menu -> Switch | SecondActivity opens with default fallback data | Pass |

## 13. Common Errors and Solutions
- **Error**: `ActivityNotFoundException` when calling `startActivity(intent)`.
  - **Solution**: Ensure `<activity android:name=".SecondActivity" />` is declared in `AndroidManifest.xml`.
- **Error**: `getIntExtra()` always returns 0 or default value.
  - **Solution**: Verify that the exact key string used in `putExtra()` matches the key string in `getIntExtra()`.
- **Error**: App crashes when pressing Back.
  - **Solution**: Avoid recreating activities unnecessarily; call `finish()` to return to the existing parent activity on the back stack.

## 14. Viva Questions and Answers
1. **Q: What is an Explicit Intent?**
   - **A**: An explicit intent specifies the target component class name directly. It is typically used for internal application navigation between known activities.
2. **Q: How does `getIntExtra()` differ from `getStringExtra()` in handling missing keys?**
   - **A**: `getStringExtra(key)` returns `null` if the key is absent. Because `int` is a primitive type that cannot be null, `getIntExtra(key, defaultValue)` requires a second parameter specifying a default fallback value.
3. **Q: What happens to `MainActivity` when `SecondActivity` is launched?**
   - **A**: `MainActivity` goes through the lifecycle transition `onPause()` followed by `onStop()`, and remains paused in the back stack while `SecondActivity` is created, started, and resumed (`onCreate()` -> `onStart()` -> `onResume()`).
4. **Q: Why should Intent extra keys use package-prefixed constants?**
   - **A**: Using package-prefixed constants (e.g. `com.example.practical04.EXTRA_NAME`) prevents namespace collisions with other apps or internal libraries that might access the intent.
5. **Q: What method is used to return data back from SecondActivity to MainActivity?**
   - **A**: `startActivityForResult()` (or modern `ActivityResultLauncher`), with the child activity calling `setResult(RESULT_OK, resultIntent)` before `finish()`.
