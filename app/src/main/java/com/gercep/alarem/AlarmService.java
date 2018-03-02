package com.gercep.alarem;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

import fragment.AlarmFragment;

import static android.media.CamcorderProfile.get;

/**
 * Created by Gilang PC on 23/02/2018.
 */

public class AlarmService extends IntentService {
    Globals g = Globals.getInstance();

    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public AlarmService() {
        super("AlarmService");
    }

    private boolean LagiDiAlaram() {
        for (int i = 0; i < g.getListLength(); i++) {
            Calendar cal2 = Calendar.getInstance();
            if (g.getListJam(i) == cal2.get(Calendar.HOUR_OF_DAY) && g.getListMenit(i) == cal2.get(Calendar.MINUTE)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // get all time

        while (true) {
            if (g.getJarak() < 0.4 && LagiDiAlaram() && !g.getTerSwipe()) {
                vibrator.vibrate(250);
                Log.i("Gilang ", "BUNYI");
            } else {
                Log.i("Gilang ", "DIEM");
            }

            Calendar cal2 = Calendar.getInstance();
            if (g.getTerSwipe()) {
                int revar = cal2.get(Calendar.MINUTE);
                while (cal2.get(Calendar.MINUTE) == revar) {
                    cal2 = Calendar.getInstance();
                }
                g.setTerSwipe(false);
            }

            if (g.getTerShake()) {
                int revar = cal2.get(Calendar.MINUTE);
                while (cal2.get(Calendar.MINUTE) == revar) {
                    cal2 = Calendar.getInstance();
                }
                g.setTerShake(false);
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
