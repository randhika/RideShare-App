package com.rideshare.rideshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.Notification;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends ArrayAdapter<Notification>{

    private List<Notification> notifications;
    private int resource;

    public NotificationAdapter(Context context, int resource, List<Notification> notifications) {
        super(context, resource, notifications);

        this.notifications = notifications;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notification notification = notifications.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        String date = "";
        ((TextView) convertView.findViewById(R.id.noti_header)).setText(notification.getHeader());
        ((TextView) convertView.findViewById(R.id.noti_body)).setText(notification.getBody());
        Locale locale = getContext().getResources().getConfiguration().locale;
        try {
            date = notification.getDate(locale);
        } catch (ParseException ignored) {}

        ((TextView) convertView.findViewById(R.id.noti_date)).setText(date);

        return convertView;
    }
}
