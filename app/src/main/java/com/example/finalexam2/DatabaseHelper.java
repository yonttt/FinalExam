package com.example.finalexam2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentEnrollment.db";
    private static final int DATABASE_VERSION = 1;

    // Student Table
    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_STUDENT_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";

    // Enrollment Table
    public static final String TABLE_ENROLLMENTS = "enrollments";
    public static final String COLUMN_ENROLLMENT_ID = "enrollment_id";
    public static final String COLUMN_STUDENT_USERNAME = "student_username";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_CREDITS = "credits";

    // Create Student Table
    private static final String CREATE_STUDENTS_TABLE =
            "CREATE TABLE " + TABLE_STUDENTS + " (" +
                    COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_EMAIL + " TEXT)";

    // Create Enrollment Table
    private static final String CREATE_ENROLLMENTS_TABLE =
            "CREATE TABLE " + TABLE_ENROLLMENTS + " (" +
                    COLUMN_ENROLLMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_USERNAME + " TEXT, " +
                    COLUMN_SUBJECT + " TEXT, " +
                    COLUMN_CREDITS + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_STUDENT_USERNAME + ") REFERENCES " +
                    TABLE_STUDENTS + "(" + COLUMN_USERNAME + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_ENROLLMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        onCreate(db);
    }

    // Student Registration
    public boolean registerStudent(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_STUDENTS, null, contentValues);
        return result != -1;
    }

    // Student Login
    public boolean loginStudent(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_STUDENTS +
                        " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{username, password}
        );

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Enroll in Subject
    public boolean enrollSubject(String username, String subject, int credits) {
        // Check total credits before enrollment
        if (getTotalEnrolledCredits(username) + credits > 24) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STUDENT_USERNAME, username);
        contentValues.put(COLUMN_SUBJECT, subject);
        contentValues.put(COLUMN_CREDITS, credits);

        long result = db.insert(TABLE_ENROLLMENTS, null, contentValues);
        return result != -1;
    }

    // Get Total Enrolled Credits
    public int getTotalEnrolledCredits(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + COLUMN_CREDITS + ") AS total_credits FROM " +
                        TABLE_ENROLLMENTS + " WHERE " + COLUMN_STUDENT_USERNAME + " = ?",
                new String[]{username}
        );

        int totalCredits = 0;
        if (cursor.moveToFirst()) {
            totalCredits = cursor.getInt(cursor.getColumnIndex("total_credits"));
        }
        cursor.close();
        return totalCredits;
    }

    // Get Enrollment Summary
    public Cursor getEnrollmentSummary(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT " + COLUMN_SUBJECT + ", " + COLUMN_CREDITS +
                        " FROM " + TABLE_ENROLLMENTS +
                        " WHERE " + COLUMN_STUDENT_USERNAME + " = ?",
                new String[]{username}
        );
    }
}
