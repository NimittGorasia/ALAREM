package com.gercep.alarem;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Gilang PC on 23/02/2018.
 */

public class DiscreteService extends IntentService {
    Globals g = Globals.getInstance();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public DiscreteService() {
        super("DiscreteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            GPSTracker gps = new GPSTracker(this);
            g.setCurrentLatitude((float)gps.getLatitude());
            g.setCurrentLongitude((float)gps.getLongitude());
            Log.i("User", String.valueOf(g.getCurrentLatitude()) + " " + String.valueOf(g.getCurrentLongitude()));
            g.setJarak(distance(gps.getLatitude(), gps.getLongitude(), (double)g.getRingsLatitude(), (double)g.getRingsLongitude(), "K"));
            Log.i("Atom : ", String.valueOf(g.getRingsLatitude()));
            Log.i("Jarak : ", String.valueOf(g.getJarak()));
            try {
                Thread.sleep(3000);
            } catch (Exception e) {}
        }

    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
