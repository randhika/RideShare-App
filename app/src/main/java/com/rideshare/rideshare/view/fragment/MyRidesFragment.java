package com.rideshare.rideshare.view.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.MyTripAdapter;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.present.MyRidesPresent;
import com.rideshare.rideshare.view.activity.NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;

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
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                parent.showContextMenuForChild(view);
            }
        });
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getGroupId() == 1){
            switch(item.getOrder()){
                case(1): updateRequest(info.position); break;
                case(2): viewSuggestions(info.position); break;
            }
        } else if(item.getGroupId() == 2){
            switch(item.getOrder()){
                case(1): viewRiders(info.position); break;
            }
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Options");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Trip trip = trips.get(info.position);
        if(trip.getType().equals("request")){
            menu.add(1, v.getId(), 1, "Update Request");
            switch (trip.getStatus()){
                case(0): menu.add(1, v.getId(), 2, "View Suggestions");
                    menu.add(1, v.getId(), 5, "Delete Request"); break;
                case(1): menu.add(1, v.getId(), 3, "View Approved Ride");
                    menu.add(1, v.getId(), 5, "Delete Request"); break;
                case(2): menu.add(1, v.getId(), 4, "Rank Driver"); break;
            }
        } else {
            switch (trip.getStatus()){
                case(0): menu.add(2, v.getId(), 1, "See Passengers Details");
                    menu.add(2, v.getId(), 4, "Delete Ride"); break;
                case(1): menu.add(2, v.getId(), 1, "See Passengers Details");
                    menu.add(2, v.getId(), 4, "Delete Ride"); break;
                case(2): menu.add(2, v.getId(), 3, "Rank Passengers"); break;
            }
        }
    }

    private void updateRequest(int position) {
        Bundle bundle = new Bundle();
        Trip trip = trips.get(position);
        try{
            JSONObject request = trip.toJsonRequest();
            String requestStr = request.toString();
            bundle.putString("TRIP", requestStr);
            ((NavigationActivity) getActivity()).selectItem(10, bundle);
        } catch (JSONException e) {
            Log.e("updateRequest", "JSONException", e);
        }
    }

    private void viewSuggestions(int position) {
        Bundle bundle = new Bundle();
        Trip trip = trips.get(position);
        bundle.putString("REQUEST_ID", trip.getId());
        ((NavigationActivity) getActivity()).selectItem(11, bundle);
    }

    private void viewRiders(int position) {
        Bundle bundle = new Bundle();
        Trip trip = trips.get(position);
        bundle.putString("RIDE_ID", trip.getId());
        ((NavigationActivity) getActivity()).selectItem(12, bundle);
    }
}
