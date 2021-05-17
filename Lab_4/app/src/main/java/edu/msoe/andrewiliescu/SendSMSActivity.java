/*
 * SendSMSActivity
 * Author: Andrew Iliescu
 * Date: 4/6/21
 * This file is a secondary Activity that gets called when the user wants to send their GPA
 * via sms to another device. This page only shows the current GPA and has a text field for the
 * phone number and a button to send the message
 */
package edu.msoe.andrewiliescu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendSMSActivity extends AppCompatActivity {
    private String formatted_gpa;

    /**
     * This is the main method that launches when this Activity is navigated to
     *
     * @param savedInstanceState the default state this Activity should boot into
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_s_m_s);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        TextView gpaTB = findViewById(R.id.show_GPA);
        EditText phone_numberTB = findViewById(R.id.phoneNumTB);
        Button send_gpa = findViewById(R.id.sendSMS_btn);
        //Ensures the gpa is a valid size
        if (intent.getStringExtra("gpa_value").length() < 4) {
            formatted_gpa = intent.getStringExtra("gpa_value").substring(0, 3);
        } else {
            formatted_gpa = intent.getStringExtra("gpa_value").substring(0, 4);
        }

        gpaTB.setText("Current GPA: " + formatted_gpa);

        send_gpa.setOnClickListener(v -> {
            sendSMS(phone_numberTB.getText().toString(), "Current GPA is " + formatted_gpa);
            finish();
        });
    }

    /**
     * This method is called to send the sms message
     *
     * @param phone_number the desired recipients phone number
     * @param msg          the desired message to be sent
     */
    private void sendSMS(String phone_number, String msg) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone_number, null, msg, null, null);
    }
}