package com.gercep.alarem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.microedition.khronos.opengles.GL;

import dataClass.User;
import fragment.AboutFragment;
import fragment.AlarmFragment;
import fragment.SettingFragment;
import fragment.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private DatabaseReference mDatabase;
    private SharedPreferences.Editor userPrefEditor;
    private SharedPreferences userPref;
    private String mEmail;

    AlarmFragment alarmFragment;
    SettingFragment settingFragment;
    AboutFragment aboutFragment;
    MenuItem prevMenuItem;

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

//    private static final String PREFS_NAME = "prefs";
//    private static final String PREF_DARK_THEME = "dark_theme";

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        }
        startService(new Intent(this, DiscreteService.class));
        startService(new Intent(this, AlarmService.class));
        startService(new Intent(this, ProxService.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);
//
//        if(useDarkTheme) {
//            setTheme(R.style.AppTheme_Dark_NoActionBar);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userPrefEditor = getSharedPreferences("user", MODE_PRIVATE).edit();
        userPref = getSharedPreferences("user", MODE_PRIVATE);
        mEmail = userPref.getString("email","12345");



        viewPager = (ViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_alarm:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_setting:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_about:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        setupViewPager(viewPager);


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                handleShakeEvent(count);
            }

            private void handleShakeEvent(int count) {
                Globals g = Globals.getInstance();
                g.setTerShake(true);

                Log.i("SHAKES", "hehe");
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        alarmFragment = new AlarmFragment();
        settingFragment = new SettingFragment();
        aboutFragment = new AboutFragment();
        adapter.addFragment(alarmFragment);
        adapter.addFragment(settingFragment);
        adapter.addFragment(aboutFragment);
        viewPager.setAdapter(adapter);
    }

//    public void toggleTheme (boolean darkTheme) {
//        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putBoolean(PREF_DARK_THEME, darkTheme);
//        editor.apply();
//
//        Intent intent = getIntent();
//        finish();
//
//        startActivity(intent);
//    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}