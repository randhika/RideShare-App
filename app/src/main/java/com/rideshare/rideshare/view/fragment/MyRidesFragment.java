package com.rideshare.rideshare.view.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.MyTripAdapter;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.present.MyRidesPresent;

import java.util.ArrayList;

public class MyRidesFragment extends ListFragment {

    private String USER_ID;
    private String next_trip_url;

    private MyRidesPresent present;
    private ArrayList<Trip> trips;
    private MyTripAdapter adapter;

    public static MyRidesFragment newInstance(Bundle args) {
        MyRidesFragment fragment = new MyRidesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MyRidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        present = new MyRidesPresent(this);
        trips = new ArrayList<>();
        adapter = new MyTripAdapter(getActivity(), trips, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_rides, container, false);
        Bundle bundle = getArguments();
        USER_ID = bundle.getString("USER");
        this.setListAdapter(adapter);
        present.getTrips(USER_ID);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        listView.setDivider(null);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateTips(String nextUrl, ArrayList<Trip> myTrips) {
        next_trip_url = nextUrl;
        trips.addAll(myTrips);
        adapter.notifyDataSetChanged();
    }
}
