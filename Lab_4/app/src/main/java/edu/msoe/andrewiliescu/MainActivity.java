/*
 * MainActivity
 * Author: Andrew Iliescu
 * Date: 4/6/21
 * This file is the main Activity that greets the user upon launching the app
 * and also where all of the data will be displayed for the user, including the
 * user defined Course objects and their respective GPA
 */
package edu.msoe.andrewiliescu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private float totalGrade = 0;
    private static final float HIGH_HONOR = 3.7f;
    private static final float HONOR_ROLL = 3.2f;
    private static final float PROBATION = 2.0f;
    private ArrayList<Course> courseList = new ArrayList<>();
    private CourseAdapter adapter;
    private TextView gpaText;
    private float gpa;
    private final DecimalFormat df = new DecimalFormat("#.##");
    private Toolbar toolbar;
    private static final int SEND_SMS_PERMISSIONS_REQUEST = 1;

    /**
     * This is the main method that launches the app
     *
     * @param savedInstanceState the default state the app should boot into
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        gpaText = findViewById(R.id.gpaTB);
        ListView list = findViewById(R.id.listView);

        //Check to see if app should be restored
        if (savedInstanceState != null) {
            courseList = savedInstanceState.getParcelableArrayList("list");
            gpa = savedInstanceState.getFloat("GPA");
            gpaText.setText("GPA: " + df.format(gpa));
        }

        //Checks to ensure there is permission to send SMS messages given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToSendSMS();
        }

        adapter = new CourseAdapter(this, courseList);
        list.setAdapter(adapter);

        //Navigate to Activity that user add data for class
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

                //Creating the necessary variables to handle notification changes
                NotificationManagerCompat nManager = NotificationManagerCompat.from(getApplicationContext());
                Uri webpage = Uri.parse("https://catalog.msoe.edu/content.php?catoid=22&navoid=623");
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                NotificationCompat.Builder builder;

                //Calculate GPA values
                for (int i = 0; i < courseList.size(); i++) {
                    totalCreditHours += courseList.get(i).getCredits();
                    calcGrade(courseList.get(i).getGrade(), courseList.get(i).getCredits());
                }
                gpa = (totalGrade / totalCreditHours);
                gpaText.setText("GPA: " + df.format(gpa));

                //Personal notification message based on their GPA
                if (gpa >= HIGH_HONOR) {
                    builder = createNotification("high_honor", "Congratulations!",
                            "You made the MSOE High Honor Roll. For more info, click here...", pIntent);
                    createNotificationChannel("high_honor");
                    nManager.notify(100, builder.build());
                } else if (gpa >= HONOR_ROLL) {
                    builder = createNotification("honor_roll", "Congratulations!",
                            "You made the MSOE Honor Roll. For more info, click here...", pIntent);
                    createNotificationChannel("honor_roll");
                    nManager.notify(100, builder.build());
                } else if (gpa < PROBATION) {
                    builder = createNotification("probation", "Immediate Action Required",
                            "You are on academic probation. For more info, click here...", pIntent);
                    createNotificationChannel("probation");
                    nManager.notify(100, builder.build());
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

    /**
     * This method is called to create the overflow menu for the app
     *
     * @param menu reference to overflow menu
     * @return true for completion
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        //Change the overflow icon's image
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_share_24);
        toolbar.setOverflowIcon(drawable);
        return true;
    }

    /**
     * This method handles what happens when a MenuItem is pressed
     *
     * @param item the MenuItem pressed
     * @return true for completion
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_sendGPA) {
            //Navigate to Activity where user enters SMS info
            Intent intent = new Intent(this, SendSMSActivity.class);
            intent.putExtra("gpa_value", String.valueOf(gpa));
            startActivity(intent);
        }
        return true;
    }

    /**
     * This method is used to generate a notification alert for the user
     *
     * @param id     the unique channel id
     * @param title  what the user should see
     * @param text   the body of the notification message
     * @param intent intent the intent for what the notification should lead to
     * @return the Notification.Builder to be passed into NotificationManager
     */
    private NotificationCompat.Builder createNotification(String id, String title, String text, PendingIntent intent) {
        return new NotificationCompat.Builder(getApplicationContext(), id)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24).setContentTitle(title)
                .setContentText(text).setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(intent)
                .setAutoCancel(true);
    }

    /**
     * This is a private method to create the channel for which notifications will be sent on
     *
     * @param id This is the unique id for the type of notification to be sent
     */
    private void createNotificationChannel(String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, "GPA_Calc", importance);
            channel.setDescription("GPA CALC Notification Channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * This method is called to check if sending sms permission is given and to allow it
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToSendSMS() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "Please allow permission.", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSIONS_REQUEST);
        }
    }
}