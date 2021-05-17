/**
 * LAB 2: GPA Calculator
 * Author: Andrew Iliescu
 * Date: 3/23/21
 */
package edu.msoe.andrewiliescu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private float totalGrade = 0;
    private float totalCreditHours = 0;
    private float gpa = 0;
    private static final float DEANS_LIST = 3.2f;
    private static final float PROBATION = 2.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView title = (TextView) findViewById(R.id.titleText);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

//        EditText classTextBox1 = (EditText) findViewById(R.id.classTextBox1);
//        EditText classTextBox2 = (EditText) findViewById(R.id.classTextBox2);
//        EditText classTextBox3 = (EditText) findViewById(R.id.classTextBox3);
//        EditText classTextBox4 = (EditText) findViewById(R.id.classTextBox4);

        EditText creditsTextBox1 = (EditText) findViewById(R.id.creditsTextBox1);
        EditText creditsTextBox2 = (EditText) findViewById(R.id.creditsTextBox2);
        EditText creditsTextBox3 = (EditText) findViewById(R.id.creditsTextBox3);
        EditText creditsTextBox4 = (EditText) findViewById(R.id.creditsTextBox4);

        EditText gradeTextBox1 = (EditText) findViewById(R.id.gradeTextBox1);
        EditText gradeTextBox2 = (EditText) findViewById(R.id.gradeTextBox2);
        EditText gradeTextBox3 = (EditText) findViewById(R.id.gradeTextBox3);
        EditText gradeTextBox4 = (EditText) findViewById(R.id.gradeTextBox4);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        Button btnCalc = (Button) findViewById(R.id.btnCalc);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalCreditHours = 0;
                totalGrade = 0;
                gpa = 0;
                totalCreditHours += Integer.parseInt(creditsTextBox1.getText().toString());
                totalCreditHours += Integer.parseInt(creditsTextBox2.getText().toString());
                totalCreditHours += Integer.parseInt(creditsTextBox3.getText().toString());
                totalCreditHours += Integer.parseInt(creditsTextBox4.getText().toString());

                gradeChecker(gradeTextBox1.getText().toString(), Integer.parseInt(creditsTextBox1.getText().toString()));
                gradeChecker(gradeTextBox2.getText().toString(), Integer.parseInt(creditsTextBox2.getText().toString()));
                gradeChecker(gradeTextBox3.getText().toString(), Integer.parseInt(creditsTextBox3.getText().toString()));
                gradeChecker(gradeTextBox4.getText().toString(), Integer.parseInt(creditsTextBox4.getText().toString()));

                gpa = (totalGrade / totalCreditHours);

                dialog.setTitle("GPA: " + gpa);
                dialog.show();

                if (gpa >= DEANS_LIST) {
                    Snackbar.make(v, "Congratulations on making Dean's List!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (gpa <= PROBATION) {
                    Snackbar.make(v, "You are on academic probation :(", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    //Private method to convert the entered letter grade to its corresponding grade points
    private void gradeChecker(String letterGrade, int classCredit) {
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
                totalGrade += (1 * classCredit);
                break;
            case "F":
                totalGrade += (0 * classCredit);
                break;
            default:
                break;
        }
    }
}