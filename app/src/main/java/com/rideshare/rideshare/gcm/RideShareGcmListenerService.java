package com.rideshare.rideshare.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class RideShareGcmListenerService extends GcmListenerService {
    public RideShareGcmListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        sendNotification(message);
    }

    private void sendNotification(String message) {

    }
}
