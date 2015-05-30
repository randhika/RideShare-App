package com.rideshare.rideshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.RideStop;
import com.rideshare.rideshare.view.fragment.TripPlannerFragment;

import java.util.List;

public class AddStopAdapter extends ArrayAdapter<RideStop>{

    private List<RideStop> stops;
    private int resource;
    private TripPlannerFragment fragment;

    public AddStopAdapter(Context context, int resource, List<RideStop> stops,
                          TripPlannerFragment fragment) {
        super(context, resource, stops);
        this.stops = stops;
        this.resource = resource;
        this.fragment = fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RideStop rideStop = stops.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        String address = rideStop.getAddress();
        String positionStr = "Stop " + position;
        String price = "Price Until Destination: " + rideStop.getPrice() + " NIS";
        String time = "Time to Destination: " + rideStop.getTime() + " Minuets";

        ((TextView) convertView.findViewById(R.id.list_stop_number)).setText(positionStr);
        ((TextView) convertView.findViewById(R.id.list_stop_address)).setText(address);
        ((TextView) convertView.findViewById(R.id.list_stop_price)).setText(price);
        ((TextView) convertView.findViewById(R.id.list_stop_time)).setText(time);
        ImageButton deleteBtn = ((ImageButton) convertView.findViewById(R.id.list_delete_btn));
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.onDeleteStop(position);
            }
        });

        return convertView;
    }
}
