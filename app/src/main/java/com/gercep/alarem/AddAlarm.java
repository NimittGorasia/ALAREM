package com.gercep.alarem;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAlarm extends AppCompatActivity {
    DatabaseReference mDatabase;
    SharedPreferences userPref;
    SharedPreferences.Editor userPrefEditor;
    private String mEmail;
    private String nodeIndex;
    private TimePicker timePicker;
    private String alarmTime;

    Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        Button setAlarm = findViewById(R.id.setAlarmButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        userPref = getSharedPreferences("user", MODE_PRIVATE);
        userPrefEditor = getSharedPreferences("user", MODE_PRIVATE).edit();

        mEmail = userPref.getString("email", "12345");
        nodeIndex = mEmail.replace(".", "%");

        timePicker = (TimePicker) findViewById(R.id.digitalClock);



        setAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Integer hour, minute;
                if(Build.VERSION.SDK_INT < 23) {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                } else {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }

                String sHour = hour.toString();
                String sMinute = minute.toString();

                if (sHour.length() == 1) {
                  sHour = "0"+sHour;
                }

                if (sMinute.length() == 1) {
                  sMinute = "0"+sMinute;
                }
                alarmTime = String.format("%s:%s", sHour, sMinute);
                Log.d("DEBUG ADD ALARM", alarmTime);

                int isMenit = 0;
                for (String retval: alarmTime.split(":")) {
                    if (isMenit == 0) {
                        g.addListJam(Integer.parseInt(retval));
                    } else { // isMenit == 1
                        g.addListMenit(Integer.parseInt(retval));
                    }
                    isMenit++;
                }

                List<String> arrayWaktu = new ArrayList<>();
                Integer waktuSize = userPref.getInt("waktuSize", 12);
                for(Integer i = 0; i < waktuSize; i++) {
                  String alarm = userPref.getString("waktu-" + i.toString(), "12345");
                  arrayWaktu.add(alarm);
                }
                arrayWaktu.add(alarmTime);
                userPrefEditor.putString("waktu-" + waktuSize.toString(), alarmTime);
                userPrefEditor.putInt("waktuSize", waktuSize+1);
                userPrefEditor.apply();
                mDatabase.child("users").child(nodeIndex).child("waktu").setValue(arrayWaktu);
                finish();
            }
        });
    }
}
