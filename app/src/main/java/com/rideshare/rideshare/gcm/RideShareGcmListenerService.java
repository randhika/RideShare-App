package com.rideshare.rideshare.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.gcm.GcmListenerService;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.view.activity.LoginActivity;

public class RideShareGcmListenerService extends GcmListenerService {
    public RideShareGcmListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String header = data.getString("header");
        String body = data.getString("body");
        sendNotification(header, body);
    }

    private void sendNotification(String header, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(header).setContentText(body);
        Intent resultIntent = new Intent(this, LoginActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        notificationManager.notify(100, builder.build());
    }
}
