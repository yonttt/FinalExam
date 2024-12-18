package com.example.finalexam2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentActivity extends AppCompatActivity {
    private Spinner subjectSpinner;
    private Button enrollButton, summaryButton;
    private TextView creditsTextView;
    private DatabaseHelper dbHelper;
    private String username;

    // Sample Subjects with Credits
    private final String[] subjects = {
            "Mathematics (4 Credits)",
            "Computer Science (3 Credits)",
            "Physics (4 Credits)",
            "Chemistry (3 Credits)",
            "English Literature (2 Credits)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        dbHelper = new DatabaseHelper(this);
        username = getIntent().getStringExtra("USERNAME");

        subjectSpinner = findViewById(R.id.subject_spinner);
        enrollButton = findViewById(R.id.enroll_button);
        summaryButton = findViewById(R.id.summary_button);
        creditsTextView = findViewById(R.id.credits_text);

        // Setup Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                subjects
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(adapter);

        // Update Credits Text
        updateCreditsDisplay();

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSubject = subjectSpinner.getSelectedItem().toString();
                String[] subjectParts = selectedSubject.split(" \\(");
                String subject = subjectParts[0];
                int credits = Integer.parseInt(subjectParts[1].replace(" Credits)", ""));

                if (dbHelper.enrollSubject(username, subject, credits)) {
                    Toast.makeText(EnrollmentActivity.this, "Enrolled Successfully", Toast.LENGTH_SHORT).show();
                    updateCreditsDisplay();
                } else {
                    Toast.makeText(EnrollmentActivity.this, "Enrollment Failed. Exceeds Credit Limit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        summaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrollmentActivity.this, EnrollmentSummaryActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });
    }

    private void updateCreditsDisplay() {
        int totalCredits = dbHelper.getTotalEnrolledCredits(username);
        creditsTextView.setText("Total Credits: " + totalCredits + " / 24");
    }
}
