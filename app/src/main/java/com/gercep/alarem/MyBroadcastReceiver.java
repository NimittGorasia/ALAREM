package com.gercep.alarem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by Gilang PC on 19/02/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Waktunya WOY!", Toast.LENGTH_SHORT).show();

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

//        for (int idx = 0; idx < 4; idx++) {
        vibrator.vibrate(5000);
//            SystemClock.sleep(1000);
//        }
    }
}
