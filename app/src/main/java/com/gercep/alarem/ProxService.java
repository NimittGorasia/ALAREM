package com.gercep.alarem;

/**
 * Created by Gilang PC on 23/02/2018.
 */

import android.app.Service;
import android.hardware.SensorEventListener;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import javax.microedition.khronos.opengles.GL;

public class ProxService extends Service implements SensorEventListener {

    @Override
    public void onCreate() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null)
            stopSelf();
        else
            sensorManager
                    .registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Globals g = Globals.getInstance();
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < event.sensor.getMaximumRange()) {

                g.setTerSwipe(true);

                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP, "CHESS");
                wl.acquire();
                try {
                    Thread.sleep(1 * 1000); // 30 seconds
                } catch (Exception e) {
                } finally {
                    wl.release();
                }
                Log.i("proximity", " hehe");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}