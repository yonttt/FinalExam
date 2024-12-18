package com.example.finalexam2;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class EnrollmentSummaryActivity extends AppCompatActivity {
    private ListView summaryListView;
    private TextView totalCreditsTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        dbHelper = new DatabaseHelper(this);
        String username = getIntent().getStringExtra("USERNAME");

        summaryListView = findViewById(R.id.summary_list_view);
        totalCreditsTextView = findViewById(R.id.total_credits_text_view);

        // Get Enrollment Summary
        Cursor cursor = dbHelper.getEnrollmentSummary(username);
        ArrayList<String> enrollmentList = new ArrayList<>();
        int totalCredits = 0;

        while (cursor.moveToNext()) {
            String subject = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SUBJECT));
            int credits = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_CREDITS));
            enrollmentList.add(subject + " (" + credits + " Credits)");
            totalCredits += credits;
        }
        cursor.close();

        // Setup ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                enrollmentList
        );
        summaryListView.setAdapter(adapter);

        // Display Total Credits
        totalCreditsTextView.setText("Total Credits: " + totalCredits + " / 24");
    }
}