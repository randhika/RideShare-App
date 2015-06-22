package com.rideshare.rideshare.view.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.ConnectedRidesAdapter;
import com.rideshare.rideshare.entity.app.Suggestion;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.present.ConnectedRidesPresent;

import java.util.ArrayList;

public class ConnectedRidesFragment extends ListFragment {

    private ConnectedRidesPresent present;
    private ArrayList<Suggestion> rides;
    private ConnectedRidesAdapter adapter;

    public static ConnectedRidesFragment newInstance(Bundle args) {
        ConnectedRidesFragment fragment = new ConnectedRidesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ConnectedRidesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String userID = args.getString("USER_ID");
        String requestID = args.getString("REQUEST_ID");
        int requestStatus = args.getInt("REQUEST_STATUS");
        rides = new ArrayList<>();
        present = new ConnectedRidesPresent(userID, requestID, requestStatus, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connected_rides, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        listView.setDivider(null);
        adapter = new ConnectedRidesAdapter(getActivity(), R.layout.connected_rides_item, rides,
                present);
        this.setListAdapter(adapter);
        present.getConnectedRides();
    }

    public void show(ArrayList<Suggestion> rides) {
        this.rides.addAll(rides);
        adapter.notifyDataSetChanged();
    }

    public void clear(){
        this.rides.clear();
    }

    public void reload(String rideID) {
        for(Suggestion s : rides){
            if(s.getRideID().equals(rideID)){
                rides.remove(s);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
