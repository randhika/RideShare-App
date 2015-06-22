package com.rideshare.rideshare.present;

import android.os.AsyncTask;
import com.rideshare.rideshare.manager.IdManager;
import com.rideshare.rideshare.entity.AppResponse;
import com.rideshare.rideshare.entity.Convertor;
import com.rideshare.rideshare.entity.app.Notification;
import com.rideshare.rideshare.view.fragment.NotificationFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class NotificationPresent {

    private NotificationFragment parent;
    private IdManager idManager;
    private NotificationGrabber notificationAsync;

    public NotificationPresent(NotificationFragment fragment){
        this.parent = fragment;
        idManager = new IdManager();
    }

    public void parseNotifications(String notifications){
        try {
            JSONObject notificationsJSON = new JSONObject(notifications);
            AppResponse appResponse = new AppResponse(200, notificationsJSON);
            postGetNotifications(appResponse);
        } catch (JSONException ignored) {}
    }

    private void postGetNotifications(AppResponse notificationResult){
        JSONObject metadataJSON;
        JSONArray notificationsJSON;
        String nextUrl;
        ArrayList<Notification> notifications = new ArrayList<>();

        if(!notificationResult.isValid()){
            return;
        }
        JSONObject responseJSON = notificationResult.getJSON();
        if(notificationResult.getStatus() != 200){
            return;
        }
        try {
            metadataJSON = responseJSON.getJSONObject("metadata");
            notificationsJSON = responseJSON.getJSONArray("notifications");
            int notificationsCount = notificationsJSON.length();

            for(int i = 0; i < notificationsCount; i++) {
                JSONObject notificationJSON = notificationsJSON.getJSONObject(i);
                Notification notification = Convertor.convert(notificationJSON, Notification.class);
                notifications.add(notification);
            }
        } catch (JSONException e) {
            return;
        }
        try {
            nextUrl = metadataJSON.getString("nextUrl");
        } catch (JSONException e) {
            nextUrl = null;
        }
        parent.showNotification(notifications, nextUrl);
    }

    public void getNotifications(String userId) {
        notificationAsync = new NotificationGrabber();
        notificationAsync.execute(userId);
    }

    private class NotificationGrabber extends AsyncTask<String, Void, AppResponse> {

        private boolean cancel = false;

        @Override
        protected AppResponse doInBackground(String... params) {
            AppResponse appResponse = new AppResponse();
            parent.clear();
            idManager.getNotifications(params[0], null, null, appResponse);
            return appResponse;
        }

        @Override
        protected void onPostExecute(AppResponse result) {
            notificationAsync = null;
            if(!cancel)
                postGetNotifications(result);
        }

        public void cancel() {
            this.cancel = true;
        }
    }

    public void stopAsync(){
        if(notificationAsync != null)
            notificationAsync.cancel();
    }
}
