package com.example.practical12;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Practical 12: Enterprise REST API Access
 * Fetches JSON data from a public REST API over HTTPS using HttpURLConnection
 * inside an ExecutorService worker thread and parses the response into native UI models.
 */
public class MainActivity extends AppCompatActivity {

    public static final String BASE_API_URL = "https://jsonplaceholder.typicode.com/posts/";

    private EditText etPostId;
    private Button btnFetch, btnRetry;
    private ProgressBar progressBar;
    private LinearLayout responseCard, errorCard;
    private TextView tvPostMeta, tvPostTitle, tvPostBody, tvErrorMessage;

    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPostId = findViewById(R.id.etPostId);
        btnFetch = findViewById(R.id.btnFetch);
        btnRetry = findViewById(R.id.btnRetry);
        progressBar = findViewById(R.id.progressBar);
        responseCard = findViewById(R.id.responseCard);
        errorCard = findViewById(R.id.errorCard);
        tvPostMeta = findViewById(R.id.tvPostMeta);
        tvPostTitle = findViewById(R.id.tvPostTitle);
        tvPostBody = findViewById(R.id.tvPostBody);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPostData();
            }
        });

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchPostData();
            }
        });
    }

    /**
     * Reads input Post ID and initiates background HTTP request.
     */
    private void fetchPostData() {
        String idStr = etPostId.getText().toString().trim();
        if (TextUtils.isEmpty(idStr)) {
            etPostId.setError("Please enter a Post ID (1-100)");
            etPostId.requestFocus();
            Toast.makeText(this, "Post ID cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        final int postId;
        try {
            postId = Integer.parseInt(idStr);
            if (postId <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            etPostId.setError("Enter a valid positive integer");
            etPostId.requestFocus();
            return;
        }

        // 1. Transition to Loading State
        progressBar.setVisibility(View.VISIBLE);
        btnFetch.setEnabled(false);
        errorCard.setVisibility(View.GONE);
        tvPostTitle.setText("Connecting to REST API endpoint...");
        tvPostBody.setText("");
        tvPostMeta.setText("Fetching ID: " + postId);

        final String requestUrl = BASE_API_URL + postId;

        // 2. Offload network request to background worker thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                HttpResult httpResult = executeHttpRequest(requestUrl);

                // 3. Dispatch result back to UI thread
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing() || isDestroyed()) return;

                        progressBar.setVisibility(View.GONE);
                        btnFetch.setEnabled(true);

                        if (httpResult.isSuccess) {
                            renderSuccess(httpResult.postModel);
                        } else {
                            renderError(httpResult.errorMessage);
                        }
                    }
                });
            }
        });
    }

    /**
     * Pure background HTTP network transaction using HttpURLConnection.
     */
    private HttpResult executeHttpRequest(String endpointUrl) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(endpointUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(8000); // 8 second connection timeout
            connection.setReadTimeout(8000);    // 8 second socket read timeout

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON response
                JSONObject jsonObject = new JSONObject(response.toString());
                int id = jsonObject.getInt("id");
                int userId = jsonObject.getInt("userId");
                String title = jsonObject.getString("title");
                String body = jsonObject.getString("body");

                PostModel post = new PostModel(id, userId, title, body);
                return new HttpResult(true, post, null);
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return new HttpResult(false, null, "HTTP 404: Post with specified ID does not exist on the server.");
            } else {
                return new HttpResult(false, null, "HTTP Error Code: " + responseCode + " - " + connection.getResponseMessage());
            }

        } catch (java.net.UnknownHostException e) {
            return new HttpResult(false, null, "No Internet Connection or Host Unreachable: Unable to resolve host 'jsonplaceholder.typicode.com'.");
        } catch (java.net.SocketTimeoutException e) {
            return new HttpResult(false, null, "Network Timeout: The remote server took longer than 8 seconds to respond.");
        } catch (JSONException e) {
            return new HttpResult(false, null, "JSON Parsing Failure: Server response was not in expected format.");
        } catch (Exception e) {
            return new HttpResult(false, null, "Network Exception: " + e.getLocalizedMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ignored) {}
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void renderSuccess(PostModel post) {
        errorCard.setVisibility(View.GONE);
        responseCard.setVisibility(View.VISIBLE);

        tvPostMeta.setText("Post ID: " + post.getId() + "  |  User ID: " + post.getUserId());
        tvPostTitle.setText(post.getTitle());
        tvPostBody.setText(post.getBody());

        Toast.makeText(this, "Data successfully loaded from REST API!", Toast.LENGTH_SHORT).show();
    }

    private void renderError(String message) {
        errorCard.setVisibility(View.VISIBLE);
        tvErrorMessage.setText(message);
        Toast.makeText(this, "Failed to load data from API", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    /**
     * Container to carry HTTP execution status and data.
     */
    private static class HttpResult {
        final boolean isSuccess;
        final PostModel postModel;
        final String errorMessage;

        HttpResult(boolean isSuccess, PostModel postModel, String errorMessage) {
            this.isSuccess = isSuccess;
            this.postModel = postModel;
            this.errorMessage = errorMessage;
        }
    }
}
