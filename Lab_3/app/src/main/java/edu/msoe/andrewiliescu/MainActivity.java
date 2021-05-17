/*
 * MainActivity
 * Author: Andrew Iliescu
 * Date: 3/29/21
 * This file is the main Activity that greets the user upon launching the app
 * and also where all of the data will be displayed for the user, including the
 * user defined Course objects and their respective GPA
 */
package edu.msoe.andrewiliescu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private float totalGrade = 0;
    private static final float DEANS_LIST = 3.2f;
    private static final float PROBATION = 2.0f;
    private ArrayList<Course> courseList = new ArrayList<>();
    private CourseAdapter adapter;
    private TextView gpaText;
    private float gpa;
    private final DecimalFormat df = new DecimalFormat("#.##");

    /**
     * This is the main method that launches the app
     *
     * @param savedInstanceState the default state the app should boot into
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        gpaText = findViewById(R.id.gpaTB);
        ListView list = findViewById(R.id.listView);

        //Check to see if app should be restored
        if (savedInstanceState != null) {
            courseList = savedInstanceState.getParcelableArrayList("list");
            gpa = savedInstanceState.getFloat("GPA");
            gpaText.setText("GPA: " + df.format(gpa));
        }

        adapter = new CourseAdapter(this, courseList);
        list.setAdapter(adapter);

        //Navigate to Fragment that user add data for class
        Intent intent = new Intent(this, CourseActivity.class);
        //Open CourseActivity on fab click
        fab.setOnClickListener(v -> startActivityForResult(intent, 1));
    }

    /**
     * This is the method that is called to read the data back from the second activity
     *
     * @param requestCode the returned request code for the intent that is expected
     * @param resultCode  ensuring that the data is returned properly
     * @param data        the intent data that get transferred between activities
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                float totalCreditHours = 0;
                totalGrade = 0;
                Course c = data.getParcelableExtra("courseData");
                courseList.add(c);
                adapter.notifyDataSetChanged();

                //Calculate GPA values
                for (int i = 0; i < courseList.size(); i++) {
                    totalCreditHours += courseList.get(i).getCredits();
                    calcGrade(courseList.get(i).getGrade(), courseList.get(i).getCredits());
                }
                gpa = (totalGrade / totalCreditHours);
                gpaText.setText("GPA: " + df.format(gpa));

                //Personal message based on their GPA
                if (gpa >= DEANS_LIST) {
                    Toast.makeText(this, "Congratulations on making Dean's List!",
                            Toast.LENGTH_LONG).show();
                } else if (gpa <= PROBATION) {
                    Toast.makeText(this, "You are on academic probation :(",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * This method is called to convert the letter grade into its equivalent grade point value
     *
     * @param letterGrade the String letter grade the user entered
     * @param classCredit the int credit count the user entered
     */
    private void calcGrade(String letterGrade, int classCredit) {
        switch (letterGrade) {
            case "A":
                totalGrade += (4 * classCredit);
                break;
            case "AB":
                totalGrade += (3.5 * classCredit);
                break;
            case "B":
                totalGrade += (3 * classCredit);
                break;
            case "BC":
                totalGrade += (2.5 * classCredit);
                break;
            case "C":
                totalGrade += (2 * classCredit);
                break;
            case "CD":
                totalGrade += (1.5 * classCredit);
                break;
            case "D":
                totalGrade += (classCredit);
                break;
            default:
                totalGrade += (0);
                break;
        }
    }

    /**
     * This method is used to save the state of the app when the phone is rotated
     * or the process is interrupted the state of the phone is saved
     *
     * @param savedInstanceState the saved data of the current phone state
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("list", courseList);
        savedInstanceState.putFloat("GPA", gpa);
    }
}