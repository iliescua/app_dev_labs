/*
 * MainActivity
 * Author: Andrew Iliescu
 * Date: 4/19/21
 * This file is the main Activity that greets the user upon launching the app
 * it has a simple UI that consists of a text box displaying the number of steps
 * walked by the user. All of the calculations and sensor readings are also handled here
 */
package edu.msoe.andrewiliescu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private TextView stepCountTB;
    private TextView humidityTB;
    private ProgressBar progressBar;
    private boolean accTemp = false;
    private boolean accLight = false;
    private boolean userAccepts = false;
    private int stepCount = 0;
    private static final int GET_ACTIVITY_PERMISSIONS_REQUEST = 1;
    private static final int WALK_VALUE = 4;
    private static final int LOW_TEMP_BOUND = 35;
    private static final int HIGH_TEMP_BOUND = 42;
    private static final int LIGHT_BOUND = 20000;
    private double previousMag = 0;
    private AlertDialog.Builder dialog;

    /**
     * This method is run when the app is first launched and sets everything up
     *
     * @param savedInstanceState the current saved state of the app
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Ensures permission to read activity data is given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadActivityData();
        }

        stepCountTB = findViewById(R.id.stepCountTB);
        progressBar = findViewById(R.id.humidityPB);
        humidityTB = findViewById(R.id.humidityPercentTB);
        dialog = new AlertDialog.Builder(MainActivity.this);
        setupAlert();
        registerSensors();
    }

    /**
     * This method is called whenever a sensor has new data and goes through and checks which sensor
     * changed and whether this chang warrants updating the step count
     *
     * @param event this variable gives access to the info on the sensor
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_LIGHT:
                accLight = event.values[0] <= LIGHT_BOUND;
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                accTemp = event.values[0] >= LOW_TEMP_BOUND && event.values[0] <= HIGH_TEMP_BOUND;
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                humidityTB.setText((int) event.values[0] + "%");
                progressBar.setProgress((int) event.values[0]);
                break;
            case Sensor.TYPE_ACCELEROMETER:
                boolean inPocket = accLight && accTemp;
                //Ensures proper conditions are met before counting steps
                if (inPocket || userAccepts) {
                    float x_accel = event.values[0];
                    float y_accel = event.values[1];
                    float z_accel = event.values[2];

                    //Calculate whether a change in acceleration warrants updating step count
                    double mag = Math.sqrt(x_accel * x_accel + y_accel * y_accel + z_accel * z_accel);
                    double deltaMag = mag - previousMag;
                    previousMag = mag;
                    if (deltaMag > WALK_VALUE) {
                        stepCount++;
                    }
                    stepCountTB.setText("Steps Walked: " + stepCount);
                }
                break;
        }
    }

    /**
     * Method that needs to be present is SensorEventListener is implemented
     *
     * @param sensor   refers to the sensor that is in use
     * @param accuracy reflects the accuracy of the sensor data present
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Called whenever this Activity is brought back into view
     */
    @Override
    protected void onResume() {
        super.onResume();
        dialog.show();
    }

    /**
     * This method is called to check if reading activity permission is given and to allow it
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadActivityData() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)) {
                Toast.makeText(this, "Please allow permission", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                    GET_ACTIVITY_PERMISSIONS_REQUEST);
        }
    }

    /**
     * This is a helper method to register the needed sensors
     */
    private void registerSensors() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Private helper method called to generate whether the user wants their steps counted
     * while using the device
     */
    private void setupAlert() {
        dialog.setPositiveButton("Ok", (dialog, which) -> userAccepts = true)
                .setNegativeButton("Cancel", (dialog, which) -> userAccepts = false);
        dialog.setTitle("Would you like to track your steps right now?");
        dialog.create();
    }
}