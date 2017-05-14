package com.example.salma.gcmemployee;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by salma on 13/05/2017.
 */

public class GCMTokenRefreshListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GCMRegistration.class);
        startService(intent);
    }
}