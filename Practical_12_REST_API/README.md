# Practical 12: Enterprise REST API Access

## 1. Aim
12. enterprise data access via internet (REST API)

## 2. Theory
Modern enterprise Android applications frequently communicate with cloud servers and microservices using RESTful web services over HTTP/HTTPS protocols.

### Architectural Principles:
1. **REST (Representational State Transfer)**: An architectural style that uses standard HTTP request verbs (`GET`, `POST`, `PUT`, `DELETE`) to transfer data formatted in JSON or XML representations.
2. **`NetworkOnMainThreadException`**: Android prevents networking on the main thread to avert UI freezing. Performing an HTTP connection on the main thread immediately throws this fatal exception.
3. **`HttpURLConnection`**: Java's standard, lightweight HTTP client supported natively in Android. It handles HTTPS TLS handshakes, redirects, streaming responses, and connection pooling.
4. **JSON (JavaScript Object Notation)**: The universal lightweight data-interchange format. Native Android includes `org.json.JSONObject` and `org.json.JSONArray` for efficient in-memory parsing.
5. **Robust State Handling**: Enterprise clients must handle four distinct UI states:
   - **Loading State**: Visual progress indicator (`ProgressBar`), disabled action triggers.
   - **Success State**: Clean presentation of parsed domain data.
   - **Empty State**: Friendly messaging when data is absent.
   - **Error / Offline State**: Gracefully catching `UnknownHostException` (offline) or `SocketTimeoutException` without crashing, coupled with a Retry mechanism.

## 3. Requirements
- **Development Environment**: Android Studio
- **Programming Language**: Java
- **Target SDK**: API 34 / 35
- **Minimum SDK**: API 23
- **Network Permissions**: `android.permission.INTERNET`, `android.permission.ACCESS_NETWORK_STATE`
- **Public REST Endpoint**: `https://jsonplaceholder.typicode.com/posts/{id}` (No private API keys needed; TLS encrypted).

## 4. Algorithm
1. **Declare Permissions**: In `AndroidManifest.xml`, declare `<uses-permission android:name="android.permission.INTERNET" />`.
2. **Collect Input**: Read Post ID (1-100) from `etPostId`.
3. **Transition to Loading**: Display `ProgressBar`, disable `btnFetch`, hide error card.
4. **Submit Worker Task**: Invoke `executorService.execute()`.
5. **Execute HTTP Request**:
   - Construct `URL("https://jsonplaceholder.typicode.com/posts/" + id)`.
   - Open connection: `(HttpURLConnection) url.openConnection()`.
   - Set request method to `"GET"`, connection timeout to `8000ms`, and read timeout to `8000ms`.
   - Verify HTTP status code \(== 200\).
   - Read byte stream into `StringBuilder` using `BufferedReader`.
6. **Parse JSON**: Pass response string to `new JSONObject(response)` and extract fields: `id`, `userId`, `title`, and `body`.
7. **Handle Exceptions**: Catch `UnknownHostException` (offline), `SocketTimeoutException` (slow network), and general `Exception` gracefully.
8. **Render UI on Main Thread**: Post data to `Handler(Looper.getMainLooper())`, populate the CardView, or show the Error Card with the Retry button.
9. **Disconnect**: Disconnect `HttpURLConnection` and close streams inside `finally` blocks.

## 5. Complete Source Code
- [MainActivity.java](file:///app/src/main/java/com/example/practical12/MainActivity.java)
- [PostModel.java](file:///app/src/main/java/com/example/practical12/PostModel.java)

## 6. XML Layouts
- [activity_main.xml](file:///app/src/main/res/layout/activity_main.xml)

## 7. AndroidManifest.xml
- [AndroidManifest.xml](file:///app/src/main/AndroidManifest.xml)

## 8. Resources
- **Strings**: [strings.xml](file:///app/src/main/res/values/strings.xml)
- **Drawables**: [card_bg.xml](file:///app/src/main/res/drawable/card_bg.xml), [button_bg.xml](file:///app/src/main/res/drawable/button_bg.xml), [ic_cloud_download.xml](file:///app/src/main/res/drawable/ic_cloud_download.xml)

## 9. Build Configuration
- [build.gradle](file:///app/build.gradle)

## 10. Execution Instructions
1. Open `Practical_12_REST_API` in Android Studio.
2. Synchronize Gradle.
3. Ensure the test device or emulator has active Internet connectivity.
4. Run the application.
5. Enter a Post ID (e.g., `1`, `5`, or `42`) and click **Fetch Data from API**.
6. To test error handling, enable Airplane Mode on your device and tap **Fetch Data from API** or **Retry Request**.

## 11. Expected Output
- **Online**: The progress bar spins briefly while the HTTP GET request executes; then the card populates with the post's title, body, ID, and user ID.
- **Offline / Airplane Mode**: An error card appears stating "No Internet Connection or Host Unreachable" without crashing the app. Tapping **Retry Request** attempts the request again.

## 12. Testing
| Test Case ID | Test Scenario | Expected Result | Status |
|---|---|---|---|
| TC_12_01 | Valid Fetch (ID = 1) | HTTP 200 OK; parses and renders title & body in CardView | Pass |
| TC_12_02 | Valid Fetch (ID = 50) | HTTP 200 OK; renders unique post #50 | Pass |
| TC_12_03 | Out-of-bounds ID (ID = 9999) | HTTP 404 handled; error card shown | Pass |
| TC_12_04 | Offline / Disconnected | `UnknownHostException` caught; friendly error card displayed; no crash | Pass |
| TC_12_05 | Retry Button Action | Triggers new fetch attempt for current input | Pass |

## 13. Common Errors and Solutions
- **Error**: `android.os.NetworkOnMainThreadException`.
  - **Solution**: Never invoke networking methods (`url.openConnection()`) on the UI thread. Use `ExecutorService`.
- **Error**: `SecurityException: Permission denied (missing INTERNET permission)`.
  - **Solution**: Ensure `<uses-permission android:name="android.permission.INTERNET" />` is declared in `AndroidManifest.xml`.
- **Error**: `Cleartext HTTP traffic not permitted`.
  - **Solution**: Use HTTPS endpoints (`https://...`). For unencrypted HTTP, declare `android:usesCleartextTraffic="true"` in the application tag.

## 14. Viva Questions and Answers
1. **Q: What is a REST API and what does REST stand for?**
   - **A**: REST stands for Representational State Transfer. It is a stateless, client-server architectural style that communicates over HTTP, using standard HTTP methods (GET, POST, PUT, DELETE) and transferring data typically in JSON format.
2. **Q: Why does Android throw `NetworkOnMainThreadException`?**
   - **A**: Network requests have unpredictable latency. Executing them on the main UI thread would block message queue processing, freeze the user interface, and lead to an Application Not Responding (ANR) crash.
3. **Q: How does `HttpURLConnection` differ from third-party libraries like Retrofit or Volley?**
   - **A**: `HttpURLConnection` is built directly into Java and native Android, requiring zero external dependencies. Libraries like Retrofit build on top of OkHttp to provide declarative interfaces, automated JSON object mapping (via Gson/Moshi), and coroutine integration.
4. **Q: What is the purpose of setting connection and read timeouts on an HTTP request?**
   - **A**: Without timeouts, an HTTP request could hang indefinitely on a stalled network connection, keeping threads alive and consuming resources. Timeouts guarantee that requests fail gracefully after a defined threshold (e.g., 8 seconds).
5. **Q: How can you support different REST endpoints or authentication in this project?**
   - **A**: Update `BASE_API_URL` to point to the desired endpoint and use `connection.setRequestProperty("Authorization", "Bearer <token>")` to attach headers.
