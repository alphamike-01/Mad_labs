package com.example.practical10;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Practical 10: Phone Contacts
 * Fetches contacts (name and phone number) from the phonebook using ContentResolver
 * and displays them in a RecyclerView with runtime permission handling.
 */
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_READ_CONTACTS = 1010;

    private TextView tvContactCount, tvEmptyMessage;
    private Button btnFetch;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewContacts;

    private ContactAdapter adapter;
    private final List<ContactModel> contactList = new ArrayList<>();

    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvContactCount = findViewById(R.id.tvContactCount);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        btnFetch = findViewById(R.id.btnFetch);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(contactList);
        recyclerViewContacts.setAdapter(adapter);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndLoadContacts();
            }
        });
    }

    /**
     * Checks READ_CONTACTS permission at runtime.
     */
    private void checkPermissionAndLoadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_REQUEST_READ_CONTACTS
            );
        } else {
            loadContactsFromPhone();
        }
    }

    /**
     * Queries device contacts via ContentResolver on background thread.
     */
    private void loadContactsFromPhone() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyMessage.setVisibility(View.GONE);
        btnFetch.setEnabled(false);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<ContactModel> fetchedContacts = new ArrayList<>();

                ContentResolver contentResolver = getContentResolver();
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String[] projection = {
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

                Cursor cursor = null;
                try {
                    cursor = contentResolver.query(uri, projection, null, null, sortOrder);
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                        do {
                            String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "Unknown";
                            String phone = (phoneIndex != -1) ? cursor.getString(phoneIndex) : "No number";

                            if (name != null) {
                                fetchedContacts.add(new ContactModel(name, phone != null ? phone : ""));
                            }
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                // Update UI on main thread
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing() || isDestroyed()) return;

                        progressBar.setVisibility(View.GONE);
                        btnFetch.setEnabled(true);

                        contactList.clear();
                        contactList.addAll(fetchedContacts);
                        adapter.notifyDataSetChanged();

                        if (contactList.isEmpty()) {
                            tvEmptyMessage.setText("No contacts found in phonebook.\nTip: On emulator, open the 'Contacts' app and add a sample contact, or click 'Create Contacts' in emulator settings.");
                            tvEmptyMessage.setVisibility(View.VISIBLE);
                            tvContactCount.setText("Total Contacts: 0");
                        } else {
                            tvEmptyMessage.setVisibility(View.GONE);
                            tvContactCount.setText("Total Contacts: " + contactList.size());
                            Toast.makeText(MainActivity.this, "Loaded " + contactList.size() + " contacts", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                loadContactsFromPhone();
            } else {
                tvEmptyMessage.setText(R.string.label_permission_denied);
                tvEmptyMessage.setVisibility(View.VISIBLE);
                Toast.makeText(this, "READ_CONTACTS permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }
}
