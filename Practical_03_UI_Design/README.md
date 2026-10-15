# Practical 03: UI Design

## 1. Aim
To design a simple user interface using linear layout / constraints layout, textview, editview, button and imageview. add drawable resources and an options menu with a settings item.

## 2. Theory
User Interface (UI) design in Android represents the visual touchpoints through which users interact with an application. Native Android applications build their user interfaces declaratively using XML layout files and connect them with Java logic through view binding.

### Key Components Used:
- **ConstraintLayout & LinearLayout**: `ConstraintLayout` allows developers to position widgets using flexible horizontal and vertical constraints relative to sibling views or parent guidelines, drastically flattening the view hierarchy and boosting rendering performance. `LinearLayout` organizes child views sequentially in either a single vertical or horizontal orientation.
- **TextView**: A user interface element that displays read-only or programmatically updated formatted text.
- **EditText**: A subclass of `TextView` configured for user editable text input. It supports varied input methods (`inputType`) such as `textPersonName`, `textEmailAddress`, and `number`.
- **Button**: A push-button widget that detects touch events and fires `OnClickListener` callbacks.
- **ImageView**: Renders bitmap images or scalable vector graphic (`VectorDrawable`) resources.
- **Drawable Resources**: XML-defined visual assets including vector graphics (`<vector>`) and state-list drawables (`<selector>`) that provide dynamic visual feedback for states like pressed, focused, or idle.
- **Options Menu**: A primary menu collection anchored to the Activity's ActionBar/Toolbar containing application actions such as Settings, Search, and About.

## 3. Requirements
- **Development Environment**: Android Studio (Ladybug / Koala / Meerkat or later)
- **Programming Language**: Java 8 / 11 / 17
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23 (Android 6.0 Marshmallow)
- **Dependencies**: `androidx.appcompat:appcompat:1.6.1`, `com.google.android.material:material:1.11.0`, `androidx.constraintlayout:constraintlayout:2.1.4`

## 4. Algorithm
1. **Start Application**: Initialize `MainActivity` extending `AppCompatActivity`.
2. **Inflate Layout**: Call `setContentView(R.layout.activity_main)` inside `onCreate()`.
3. **Bind Views**: Bind the `ImageView`, `TextView`, `EditText`, and `Button` instances using `findViewById()`.
4. **Attach Click Listeners**:
   - For `btnSubmit`: Read `etName` and `etEmail`. Validate for non-empty text and correct email regex patterns. If invalid, call `setError()` and display a `Toast`. If valid, format and set the text on `tvResult` and show a confirmation `Toast`.
   - For `btnClear`: Reset the `EditText` fields and clear output summaries.
5. **Inflate Options Menu**: Override `onCreateOptionsMenu(Menu menu)` and inflate `R.menu.menu_main`.
6. **Handle Menu Selection**: Override `onOptionsItemSelected(MenuItem item)`. If `action_settings` is selected, present an interactive `AlertDialog` allowing configuration of application preferences.
7. **End Task**: Display output to the user.

## 5. Complete Source Code
See [MainActivity.java](file:///app/src/main/java/com/example/practical03/MainActivity.java).

## 6. XML Layouts
See [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml).

## 7. AndroidManifest.xml
See [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml).

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Colors**: [colors.xml](file:///app/src/main/res/values/colors.xml)
- **Themes**: [themes.xml](file:///app/src/main/res/values/themes.xml)
- **Menu**: [menu_main.xml](file:///app/src/main/res/menu/menu_main.xml)
- **Drawables**: [ic_student_avatar.xml](file:///app/src/main/res/drawable/ic_student_avatar.xml), [custom_button_bg.xml](file:///app/src/main/res/drawable/custom_button_bg.xml), [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml)

## 9. Build Configuration
See [build.gradle](file:///app/build.gradle) and [settings.gradle](file:///settings.gradle).

## 10. Execution Instructions
1. Launch Android Studio.
2. Select **File -> Open...** and browse to this folder (`Practical_03_UI_Design`).
3. Allow Gradle to synchronize dependencies.
4. Connect an Android device or start an Android Virtual Device (AVD).
5. Click **Run 'app'** (Shift + F10).

## 11. Expected Output
- The screen displays an avatar image, title, subtitle, name field, email field, submit button, clear button, and an output summary card.
- Pressing **Submit** with valid values renders a formatted summary card and triggers a Toast notification.
- Clicking the three-dot overflow menu in the top-right corner reveals the **Settings** item. Selecting it opens an interactive multi-choice settings dialog.

## 12. Testing
| Test Case ID | Test Scenario | Input | Expected Result | Status |
|---|---|---|---|---|
| TC_03_01 | Valid Submission | Name: "John Doe", Email: "john@college.edu" | Summary card displays details; Toast: "Registration Successful!" | Pass |
| TC_03_02 | Empty Name Field | Name: "", Email: "john@college.edu" | `setError("Full name is required!")` displayed on `etName`; Toast shown | Pass |
| TC_03_03 | Invalid Email Format | Name: "John Doe", Email: "invalid_email" | `setError("Please enter a valid email address!")` on `etEmail`; Toast shown | Pass |
| TC_03_04 | Clear Button Action | Filled fields | Inputs and summary reset to default placeholders | Pass |
| TC_03_05 | Options Menu Settings | Click three dots -> Settings | Interactive AlertDialog appears with configurable checkboxes | Pass |

## 13. Common Errors and Solutions
- **Error**: `NullPointerException` on `findViewById()`.
  - **Solution**: Ensure `setContentView(R.layout.activity_main)` is called before invoking `findViewById()`.
- **Error**: Options menu not appearing in the top bar.
  - **Solution**: Ensure your theme inherits from a DarkActionBar theme (`Theme.MaterialComponents.DayNight.DarkActionBar`), or manually configure a `Toolbar` as the support ActionBar.
- **Error**: State list drawable not updating button color.
  - **Solution**: With Material Components, use standard `android:background="@drawable/custom_button_bg"` together with `app:backgroundTint="@null"` if button tinting overrides custom drawables.

## 14. Viva Questions and Answers
1. **Q: What is the primary advantage of ConstraintLayout over LinearLayout?**
   - **A**: `ConstraintLayout` enables building complex, responsive layouts with a flat view hierarchy without nesting multiple layouts. Flattening the hierarchy prevents recursive layout measuring passes, significantly improving UI rendering speed.
2. **Q: How does Android inflate an Options Menu?**
   - **A**: The Activity overrides `onCreateOptionsMenu(Menu menu)`, obtaining a `MenuInflater` via `getMenuInflater()`, and calls `inflater.inflate(R.menu.menu_resource, menu)`.
3. **Q: What is a VectorDrawable and why is it preferred over PNGs?**
   - **A**: A `VectorDrawable` is an XML-defined graphic based on points, lines, and curves with associated color information. It scales crisply to any screen density without pixelation or increasing APK file size.
4. **Q: How does `setError()` on an EditText work?**
   - **A**: `et.setError("message")` displays a warning icon and an error tooltip next to the input field, visually guiding the user to fix invalid input.
5. **Q: What is the difference between `dp` and `sp` units in Android?**
   - **A**: `dp` (density-independent pixels) scales according to physical screen density (160 dpi baseline) and is used for dimensions, margins, and view sizes. `sp` (scale-independent pixels) also scales with screen density but additionally respects the user's system-wide font size preference.
