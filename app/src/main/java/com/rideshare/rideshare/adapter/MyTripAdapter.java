package com.rideshare.rideshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.view.fragment.MyRidesFragment;

import java.util.List;

public class MyTripAdapter extends ArrayAdapter<Trip> {

    private List<Trip> trips;
    private MyRidesFragment fragment;

    public MyTripAdapter(Context context, List<Trip> trips, MyRidesFragment fragment){
        super(context, 0, trips);
        this.fragment = fragment;
        this.trips = trips;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Trip trip = trips.get(position);
        int resource;

        if(trip.getType().equals("request")){
            resource = R.layout.request_item;
        } else {
            resource = R.layout.ride_item;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        String status = "";

        if(trip.getType().equals("request")){
            switch (trip.getStatus()){
                case(0): status = "Not Accepted by Ride"; break;
                case(1): status = "Approved by Ride"; break;
                case(2): status = "Waiting For Rank"; break;
            }
        } else {
            switch (trip.getStatus()){
                case(0): status = "Waiting for Passengers"; break;
                case(1): status = "Full Ride"; break;
                case(2): status = "Waiting For Rank"; break;
            }
        }

        String destination = "Destination: " + trip.getDestination();
        String source = "Source: " + trip.getSource();
        String time = trip.getDate() + " " + trip.getTimeFrom() + " - " + trip.getTimeUntil();

        ((TextView) convertView.findViewById(R.id.header)).setText(trip.getType());
        ((TextView) convertView.findViewById(R.id.source)).setText(source);
        ((TextView) convertView.findViewById(R.id.status)).setText(status);
        ((TextView) convertView.findViewById(R.id.destination)).setText(destination);
        ((TextView) convertView.findViewById(R.id.time)).setText(time);

        Animation animationY = new TranslateAnimation(0, 0, parent.getHeight() / 8, 0);
        animationY.setDuration(300);
        convertView.startAnimation(animationY);

        return convertView;
    }
}
