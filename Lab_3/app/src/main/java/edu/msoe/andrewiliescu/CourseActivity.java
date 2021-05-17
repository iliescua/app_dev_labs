/*
 * CourseActivity
 * Author: Andrew Iliescu
 * Date: 3/29/21
 * This file creates the page the user sees when entering their
 * course information prior to it being displayed
 */
package edu.msoe.andrewiliescu;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class CourseActivity extends AppCompatActivity {
    private AutoCompleteTextView classTB;
    private EditText creditsTB;
    private EditText gradeTB;
    private final Course c = new Course();


    /**
     * This method is called to create the activity in which the user inputs their course info
     *
     * @param savedInstanceState the state that the app should boot up in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_layout);

        //Initialize XML components that are going to be manipulated by user
        classTB = findViewById(R.id.classTextBox);
        creditsTB = findViewById(R.id.creditsTextBox);
        gradeTB = findViewById(R.id.gradeTextBox);
        Button btn_add = findViewById(R.id.add_button);

        //Get string array for classes data and create an autocomplete
        Resources res = getResources();
        String[] classes = res.getStringArray(R.array.known_courses_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classes);
        classTB.setThreshold(2);
        classTB.setAdapter(adapter);

        //Return the populated Course object
        Intent intent = new Intent();

        //When ADD button clicked it will register course data
        btn_add.setOnClickListener(v -> {
            Course tempCourse = populateCourse();
            //Makes certain that bad data isn't returned in the intent
            if (tempCourse.getCredits() == 0 || !checkGrade(tempCourse.getGrade())) {
                showAlert("Check Your Credits or Grade");
            } else {
                intent.putExtra("courseData", tempCourse);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * This method populates a course object with user data
     *
     * @return the populated course object
     */
    private Course populateCourse() {
        try {
            c.setName(classTB.getText().toString());
            c.setCredits(Integer.parseInt(creditsTB.getText().toString()));
            c.setGrade(gradeTB.getText().toString().toUpperCase());
        } catch (NumberFormatException e) {
            showAlert("Make sure the values entered are valid");
        }
        return c;
    }

    /**
     * This method generates a basic alert dialog
     *
     * @param issue user specifies what the issues is when calling this method
     */
    private void showAlert(String issue) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(issue);
        alertDialog.show();
    }

    /**
     * This method is called to ensure the user inputted grade is valid
     *
     * @param grade the grade value the user enters
     * @return boolean value determining if the entered grade is valid
     */
    private boolean checkGrade(String grade) {
        String[] legit_values = {"A", "AB", "B", "BC", "C", "CD", "D", "F"};
        boolean present = false;
        for (String legit_value : legit_values) {
            if (grade.equals(legit_value)) {
                present = true;
                break;
            }
        }
        return present;
    }
}