package com.rideshare.rideshare.view.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.NotificationAdapter;
import com.rideshare.rideshare.entity.app.Notification;
import com.rideshare.rideshare.present.NotificationPresent;

import java.util.ArrayList;

public class NotificationFragment extends ListFragment {

    private String USER_ID;

    private NotificationPresent present;
    private NotificationAdapter adapter;
    private ArrayList<Notification> notifications;
    private String nextNotificationsUrl;

    public static NotificationFragment newInstance(Bundle args) {
        NotificationFragment fragment = new NotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        present = new NotificationPresent(this);
        notifications = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        Bundle bundle = getArguments();
        String notificationsStr = bundle.getString("NOTIFICATION_STR");
        USER_ID = bundle.getString("USER");

        adapter = new NotificationAdapter(getActivity(), R.layout.notification_item, notifications);
        this.setListAdapter(adapter);
        present.parseNotifications(notificationsStr);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showNotification(ArrayList<Notification> notifications, String nextUrl) {
        this.nextNotificationsUrl = nextUrl;
        this.notifications.addAll(notifications);
    }
}
