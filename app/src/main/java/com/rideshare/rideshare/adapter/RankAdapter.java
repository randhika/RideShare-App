package com.rideshare.rideshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.entity.app.Rank;
import com.rideshare.rideshare.present.RankPresent;
import java.util.List;

public class RankAdapter extends ArrayAdapter<Rank> {

    private List<Rank> ranks;
    private int resource;

    public RankAdapter(Context context, int resource, List<Rank> ranks) {
        super(context, resource, ranks);

        this.ranks = ranks;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Rank rank = ranks.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.name)).setText(rank.getName());
        ((RatingBar) convertView.findViewById(R.id.rating)).setOnRatingBarChangeListener(
            new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    rank.setRank((int) rating);
                }
            }
        );

        return convertView;
    }
}
