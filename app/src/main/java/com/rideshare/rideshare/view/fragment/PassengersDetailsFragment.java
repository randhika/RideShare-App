package com.rideshare.rideshare.view.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.RidersAdapter;
import com.rideshare.rideshare.entity.app.Rider;
import com.rideshare.rideshare.present.PassengersDetailsPresent;

import java.util.ArrayList;

public class PassengersDetailsFragment extends ListFragment {

    private ArrayList<Rider> riders;
    private PassengersDetailsPresent present;
    private RidersAdapter adapter;

    public static PassengersDetailsFragment newInstance(Bundle args) {
        PassengersDetailsFragment fragment = new PassengersDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PassengersDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String rideID = args.getString("RIDE_ID");
        riders = new ArrayList<>();
        present = new PassengersDetailsPresent(this, rideID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passengers_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        listView.setDivider(null);
        adapter = new RidersAdapter(getActivity(), R.layout.rider_item, riders, present);
        this.setListAdapter(adapter);
        present.getRiders();
    }

    public void setRiders(ArrayList<Rider> passengers, ArrayList<Rider> waitingList) {
        this.riders.addAll(passengers);
        this.riders.addAll(waitingList);
        adapter.notifyDataSetChanged();
    }

    public void clear() {
        riders.clear();
    }
}
