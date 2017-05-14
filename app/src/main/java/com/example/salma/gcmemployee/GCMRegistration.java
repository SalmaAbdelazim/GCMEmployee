package com.example.salma.gcmemployee;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by salma on 13/05/2017.
 */

public class GCMRegistration extends IntentService {
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String Sender_ID = "113768410956";
    public static String token = null;

    public GCMRegistration() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();

    }

    public void registerGCM() {
        Intent registrationComplete = null;

        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(Sender_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.w("GCMRegIntentService", "token:" + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
            subscribeTopics(token);

        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    /////set topic as employee
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);

        pubSub.subscribe(token, "/topics/emp", null);

    }

}
