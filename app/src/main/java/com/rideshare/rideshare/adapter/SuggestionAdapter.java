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
import com.rideshare.rideshare.entity.app.Suggestion;
import com.rideshare.rideshare.present.SuggestionPresent;

import java.util.List;

public class SuggestionAdapter extends ArrayAdapter<Suggestion> {

    private List<Suggestion> suggestions;
    private int resource;
    private SuggestionPresent present;

    public SuggestionAdapter(Context context, int resource, List<Suggestion> suggestions,
                             SuggestionPresent present) {
        super(context, resource, suggestions);

        this.suggestions = suggestions;
        this.resource = resource;
        this.present = present;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Suggestion suggestion = suggestions.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.suggestion_head))
                .setText("Suggestion " + position);
        ((TextView) convertView.findViewById(R.id.suggestion_source))
                .setText(suggestion.getSource());
        ((TextView) convertView.findViewById(R.id.suggestion_destination))
                .setText(suggestion.getDestination());
        ((TextView) convertView.findViewById(R.id.suggestion_time))
                .setText(suggestion.getDate() + " " + suggestion.getTimeFrom() + " - " +
                suggestion.getTimeUntil());
        ((TextView) convertView.findViewById(R.id.suggestion_destination))
                .setText(suggestion.getDestination());
        ((TextView) convertView.findViewById(R.id.suggestion_price))
                .setText("" + suggestion.getPrice() + " NIS");
        ((TextView) convertView.findViewById(R.id.suggestion_smoker))
                .setText(suggestion.getSmoker());
        ((TextView) convertView.findViewById(R.id.suggestion_seats))
                .setText("" + suggestion.getSeatsLeft());
        ((TextView) convertView.findViewById(R.id.suggestion_driver_name))
                .setText(suggestion.getDriver());
        ((TextView) convertView.findViewById(R.id.suggestion_driver_phone))
                .setText(suggestion.getDriverPhone());
        ((RatingBar) convertView.findViewById(R.id.suggestion_driver_rating))
                .setNumStars(suggestion.getDriverRank());
        ((TextView) convertView.findViewById(R.id.suggestion_car))
                .setText(suggestion.getCar());
        ((TextView) convertView.findViewById(R.id.suggestion_date))
                .setText(suggestion.getSuggestionDate());
        if(!suggestion.isPartOfRide()){
            convertView.findViewById(R.id.suggestion_part_of_ride).setVisibility(View.INVISIBLE);
        }

        convertView.findViewById(R.id.suggestion_join_btn)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        present.requestRide(suggestion);
                    }
                });
        return convertView;
    }
}
