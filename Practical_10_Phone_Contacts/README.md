# Practical 10: Phone Contacts

## 1. Aim
10.To create contacts from phone: Develop an application that fetches all contacts from phonebook and displays on screen.

## 2. Theory
Android maintains system databases such as contacts, media, and telephony behind structured **Content Providers**. To access device contacts, an application queries the Contacts Provider using a `ContentResolver`.

### Architecture Components:
1. **ContentProvider & ContentResolver**: `ContentProvider` encapsulates data storage and enforces security permissions. Client applications query data through `Context.getContentResolver()`.
2. **`ContactsContract.CommonDataKinds.Phone`**: A specialized table view in the Contacts Provider containing phone numbers linked to contact display names.
   - URI: `ContactsContract.CommonDataKinds.Phone.CONTENT_URI`
   - Projection columns: `DISPLAY_NAME`, `NUMBER`
3. **Cursor Management**: A `Cursor` provides random read-write access to the result set returned by a database query. Cursors must always be closed inside a `finally` block or try-with-resources to avoid resource leaks.
4. **Dangerous Runtime Permissions**: The `android.permission.READ_CONTACTS` permission protects user privacy and is categorized as a dangerous permission requiring explicit runtime authorization by the user (`ActivityCompat.requestPermissions()`).
5. **Background Thread Execution**: Querying phone contacts can involve hundreds or thousands of records and disk reads. Running this query inside an `ExecutorService` prevents UI thread stalls.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23
- **Dependencies**: `androidx.recyclerview:recyclerview:1.3.2`

## 4. Algorithm
1. **Declare Permission**: Declare `<uses-permission android:name="android.permission.READ_CONTACTS" />` in `AndroidManifest.xml`.
2. **Setup RecyclerView**: Bind `RecyclerView` with a `LinearLayoutManager` and custom `ContactAdapter`.
3. **Request Permission**: On button tap, check `ContextCompat.checkSelfPermission(this, READ_CONTACTS)`. If not granted, call `ActivityCompat.requestPermissions()`.
4. **Query Contacts on Worker Thread**:
   - Submit a task to `ExecutorService`.
   - Call `contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, sortOrder)`.
   - Iterate through `Cursor` rows, reading `DISPLAY_NAME` and `NUMBER`.
   - Add each contact to `List<ContactModel>`.
   - Close `Cursor` in `finally` block.
5. **Update UI**: Post results to `Handler(Looper.getMainLooper())`, notify the adapter (`notifyDataSetChanged()`), and update the count header.
6. **Handle Edge Cases**: If permission is denied or contact list is empty, display helpful fallback guidance.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical10/MainActivity.java)
- [ContactModel.java](file:///app/src/main/java/com/example/practical10/ContactModel.java)
- [ContactAdapter.java](file:///app/src/main/java/com/example/practical10/ContactAdapter.java)

## 6. XML Layouts
- [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml)
- [item_contact.xml](file:///app/src/main/res/layout/item_contact.xml)

## 7. AndroidManifest.xml
- [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml)

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [ic_contact_avatar.xml](file:///app/src/main/res/drawable/ic_contact_avatar.xml), [ic_phone.xml](file:///app/src/main/res/drawable/ic_phone.xml)

## 9. Build Configuration
- [build.gradle](file:///app/build.gradle)

## 10. Execution Instructions
1. Open `Practical_10_Phone_Contacts` in Android Studio.
2. Synchronize Gradle.
3. Run on an Android emulator or physical device.
4. When prompted, tap **Allow** for contacts access.
5. Tap **Fetch Contacts**.

### How to add contacts to an Android Emulator:
- **Method 1**: In the running emulator, open the native **Contacts** app, tap **Create Contact** (+), enter Name (e.g. "Professor Smith") and Phone Number (e.g. "9876543210"), then tap Save.
- **Method 2 (via ADB command)**:
  `adb shell am start -a android.intent.action.INSERT -t vnd.android.cursor.dir/contact -e name "Alice" -e phone "1234567890"`

## 11. Expected Output
- Tapping **Fetch Contacts** presents a runtime permission dialog.
- Upon approval, the progress indicator spins and the RecyclerView populates with all contacts formatted with an avatar icon, contact name, and phone number.
- Total contacts count is updated dynamically at the top.

## 12. Testing
| Test Case ID | Test Scenario | Expected Result | Status |
|---|---|---|---|
| TC_10_01 | First Run Permission | System permission dialog for Contacts appears | Pass |
| TC_10_02 | Permission Granted | Contacts query executed; cards populated in RecyclerView | Pass |
| TC_10_03 | Permission Denied | Informative permission denied message shown; no crash | Pass |
| TC_10_04 | Empty Phonebook | Empty message displayed with emulator contact addition instructions | Pass |
| TC_10_05 | Card Click | Toast appears displaying selected contact's details | Pass |

## 13. Common Errors and Solutions
- **Error**: `SecurityException: Permission Denial: reading com.android.providers.contacts requires android.permission.READ_CONTACTS`.
  - **Solution**: Ensure permission is declared in `AndroidManifest.xml` AND requested at runtime.
- **Error**: Empty list on freshly created emulator.
  - **Solution**: Emulators ship with an empty contact database. Add sample contacts via the emulator's Contacts app.
- **Error**: Cursor leak warnings in Logcat.
  - **Solution**: Always close the cursor in a `finally` block: `if (cursor != null) cursor.close();`.

## 14. Viva Questions and Answers
1. **Q: What is a ContentProvider in Android?**
   - **A**: A ContentProvider is one of the four core Android application components that manages access to a structured repository of data, allowing secure data sharing across different applications.
2. **Q: What is the purpose of `ContentResolver`?**
   - **A**: `ContentResolver` provides client applications with methods (`query()`, `insert()`, `update()`, `delete()`) to interact with a ContentProvider using a content URI.
3. **Q: Why is `READ_CONTACTS` classified as a dangerous permission?**
   - **A**: Dangerous permissions cover sensitive private user data that could compromise privacy or device operation. Android requires runtime approval by the user for dangerous permissions.
4. **Q: What is the benefit of RecyclerView over ListView?**
   - **A**: `RecyclerView` enforces the ViewHolder pattern, recycling views that have scrolled off-screen to minimize expensive `findViewById()` and layout inflations. It also supports flexible layout managers and item animations.
5. **Q: Why must contact querying be done in the background?**
   - **A**: Phonebooks often contain hundreds or thousands of contacts. Querying SQLite databases involves disk I/O; doing so on the main thread would freeze the UI and trigger an ANR.
