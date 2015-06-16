package com.rideshare.rideshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.Rider;
import com.rideshare.rideshare.present.PassengersDetailsPresent;
import java.util.ArrayList;
import java.util.List;

public class RidersAdapter extends ArrayAdapter<Rider> {

    private List<Rider> riders;
    private int resource;
    private PassengersDetailsPresent present;

    public RidersAdapter(Context context, int resource, ArrayList<Rider> riders,
                         PassengersDetailsPresent present) {
        super(context, resource, riders);

        this.present = present;
        this.resource = resource;
        this.riders = riders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Rider rider = riders.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        if(rider.getType() == 0){
            ((TextView) convertView.findViewById(R.id.header)).setText("Waiting for Approval");
            convertView.findViewById(R.id.del_passenger).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) convertView.findViewById(R.id.header)).setText("Passenger");
            convertView.findViewById(R.id.add_passenger).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.del_waiting_passenger).setVisibility(View.INVISIBLE);
        }

        ((TextView) convertView.findViewById(R.id.name)).setText(rider.getFullName());
        ((TextView) convertView.findViewById(R.id.phone)).setText(rider.getPhone());
        ((TextView) convertView.findViewById(R.id.source)).setText(rider.getSource());
        ((TextView) convertView.findViewById(R.id.destination)).setText(rider.getDestination());
        ((RatingBar) convertView.findViewById(R.id.rating)).setNumStars(rider.getRating());

        if(rider.getBags() != 2){
            convertView.findViewById(R.id.bags_rider).setVisibility(View.INVISIBLE);
        }

        (convertView.findViewById(R.id.add_passenger)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        present.addPassenger(rider);
                    }
                }
        );

        (convertView.findViewById(R.id.del_passenger)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        present.removePassenger(rider);
                    }
                }
        );

        (convertView.findViewById(R.id.del_waiting_passenger)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        present.removeWaitingUser(rider);
                    }
                }
        );

        return convertView;
    }
}
