/*
 * CoursesDataBase
 * Author: Andrew Iliescu
 * Date: 4/12/21
 * This file implements an SQLite Database to store the user's
 * course data as a means of persistence for my app
 */
package edu.msoe.andrewiliescu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CoursesDataBase extends SQLiteOpenHelper {

    private static final String COURSE_TABLE = "COURSE_TABLE";
    private static final String COLUMN_COURSE_NAME = "COURSE_NAME";
    private static final String COLUMN_COURSE_CREDITS = "COURSE_CREDITS";
    private static final String COLUMN_COURSE_GRADE = "COURSE_GRADE";
    private static final String COLUMN_ID = "ID";

    /**
     * Default constructor for create a CourseDataBase object
     *
     * @param context the applications context gets passed in
     */
    public CoursesDataBase(@Nullable Context context) {
        super(context, "course.db", null, 1);
    }

    /**
     * This method is run to create the Database initially
     *
     * @param db the is a reference to the database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + COURSE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_NAME + " TEXT, " + COLUMN_COURSE_CREDITS + " INTEGER, " + COLUMN_COURSE_GRADE + " TEXT)";
        db.execSQL(createTableStatement);
    }

    /**
     * This method is used when upgrade the Database with new versions
     * unused in my implementation however, it needs to be here since
     * I extended SQLiteOpenHelper
     *
     * @param db         reference to the Database
     * @param oldVersion the old version's value
     * @param newVersion the new version's value
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * This method is called to add a course to the Database
     *
     * @param course the value from the user entered course
     */
    public void addOneToDB(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_COURSE_NAME, course.getName());
        cv.put(COLUMN_COURSE_CREDITS, course.getCredits());
        cv.put(COLUMN_COURSE_GRADE, course.getGrade());

        db.insert(COURSE_TABLE, null, cv);
    }

    /**
     * This method is used to return all of the rows in the Database
     *
     * @return an ArrayList containing all of the courses
     */
    public ArrayList<Course> getAllDataDB() {
        ArrayList<Course> courseList = new ArrayList<>();

        String queryString = "SELECT * FROM " + COURSE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                String courseName = cursor.getString(1);
                int courseCredits = cursor.getInt(2);
                String courseGrade = cursor.getString(3);

                Course c = new Course(courseName, courseCredits, courseGrade);
                courseList.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courseList;
    }

    /**
     * This method is called to clear the Database of all its rows
     */
    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(COURSE_TABLE, null, null);
        db.close();
    }
}
