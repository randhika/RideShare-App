package com.rideshare.rideshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.Suggestion;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.present.ConnectedRidesPresent;
import java.util.ArrayList;
import java.util.List;

public class ConnectedRidesAdapter extends ArrayAdapter<Suggestion>{

    private List<Suggestion> rides;
    private int resource;
    private ConnectedRidesPresent present;

    public ConnectedRidesAdapter(Context context, int resource, ArrayList<Suggestion> rides,
                                 ConnectedRidesPresent present) {
        super(context, 0, rides);
        this.present = present;
        this.resource = resource;
        this.rides = rides;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Suggestion suggestion = rides.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        if(present.getRequestStatus() == 0){
            ((TextView) convertView.findViewById(R.id.head)).setText("Waiting for Approval");
        } else {
            ((TextView) convertView.findViewById(R.id.head)).setText("Approved Ride");
        }
        ((TextView) convertView.findViewById(R.id.source))
                .setText(suggestion.getSource());
        ((TextView) convertView.findViewById(R.id.destination))
                .setText(suggestion.getDestination());
        ((TextView) convertView.findViewById(R.id.time))
                .setText(suggestion.getDate() + " " + suggestion.getTimeFrom() + " - " +
                        suggestion.getTimeUntil());
        ((TextView) convertView.findViewById(R.id.destination))
                .setText(suggestion.getDestination());
        ((TextView) convertView.findViewById(R.id.price))
                .setText("" + suggestion.getPrice() + " NIS");
        ((TextView) convertView.findViewById(R.id.smoker))
                .setText(suggestion.getSmoker());
        ((TextView) convertView.findViewById(R.id.seats))
                .setText("" + suggestion.getSeatsLeft());
        ((TextView) convertView.findViewById(R.id.driver_name))
                .setText(suggestion.getDriver());
        ((TextView) convertView.findViewById(R.id.driver_phone))
                .setText(suggestion.getDriverPhone());
        ((RatingBar) convertView.findViewById(R.id.driver_rating))
                .setNumStars(suggestion.getDriverRank());
        ((TextView) convertView.findViewById(R.id.car))
                .setText(suggestion.getCar());
        ((TextView) convertView.findViewById(R.id.date))
                .setText(suggestion.getSuggestionDate());
        if(!suggestion.isPartOfRide()){
            convertView.findViewById(R.id.part_of_ride).setVisibility(View.INVISIBLE);
        }

        convertView.findViewById(R.id.remove_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        present.removeFromRide(suggestion);
                    }
                });
        return convertView;
    }
}
