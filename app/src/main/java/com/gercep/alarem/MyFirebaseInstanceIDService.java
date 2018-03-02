package com.gercep.alarem;

import android.app.Service;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by lelouch on 2/22/18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = "MY FIREBASE IID";
    private String token;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        token = FirebaseInstanceId.getInstance().getToken();
    }

    public String getToken() {
        onTokenRefresh();
        return token;
    }
}
