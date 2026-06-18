package com.example.zakatgoldcalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    // ✏️ Fill in your own details here
    private static final String DEVELOPER_NAME = "Your Full Name";
    private static final String STUDENT_ID = "Student ID: 2024XXXXXXX";
    private static final String CLASS_INFO = "ICT602 - Mobile Technology & Development";
    private static final String GITHUB_URL = "https://github.com/YOUR_USERNAME/YOUR_REPO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Show back button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About");
        }

        // Fill in developer details
        ((TextView) findViewById(R.id.tvDeveloperName)).setText(DEVELOPER_NAME);
        ((TextView) findViewById(R.id.tvStudentId)).setText(STUDENT_ID);
        ((TextView) findViewById(R.id.tvClass)).setText(CLASS_INFO);

        // Clickable GitHub URL
        TextView tvUrl = findViewById(R.id.tvAppUrl);
        tvUrl.setText(GITHUB_URL);
        tvUrl.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
            startActivity(browserIntent);
        });
    }

    // Handle back button in ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
