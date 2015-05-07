package com.rideshare.rideshare.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.AddStopAdapter;
import com.rideshare.rideshare.adapter.SpinnerAdapter;
import com.rideshare.rideshare.entity.app.RideStop;
import com.rideshare.rideshare.present.TripPlannerPresent;
import com.rideshare.rideshare.view.dialog.DatePickerFragment;
import com.rideshare.rideshare.view.dialog.StopDialog;
import com.rideshare.rideshare.view.dialog.TimePickerFragment;

import java.util.ArrayList;

public class TripPlannerFragment extends ListFragment implements View.OnClickListener,
        View.OnFocusChangeListener, AdapterView.OnItemSelectedListener{

    private final static int ADD_STOP_CODE = 100;
    private final static int DATE_CODE = 101;
    private static String USER_ID;

    private ArrayList<RideStop> stops;
    private AddStopAdapter adapter;
    private TripPlannerPresent present;

    public static TripPlannerFragment newInstance(Bundle args) {
        TripPlannerFragment fragment = new TripPlannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TripPlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        USER_ID = bundle.getString("USER");

        stops = new ArrayList<>();
        present = new TripPlannerPresent(this, USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trip_planner, container, false);
        adapter = new AddStopAdapter(getActivity(), R.layout.stop_item, stops, this);
        this.setListAdapter(adapter);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button rideBtn = (Button) view.findViewById(R.id.ride_btn);
        rideBtn.setOnClickListener(this);
        Button requestBtn = (Button) view.findViewById(R.id.request_btn);
        requestBtn.setOnClickListener(this);
        Button addStopBtn = (Button) view.findViewById(R.id.add_stop);
        addStopBtn.setOnClickListener(this);
        EditText date = (EditText) view.findViewById(R.id.date);
        date.setOnClickListener(this);
        EditText tf = (EditText) view.findViewById(R.id.time_from);
        tf.setOnClickListener(this);
        EditText tu = (EditText) view.findViewById(R.id.time_until);
        tu.setOnClickListener(this);
        EditText source = (EditText) view.findViewById(R.id.source);
        source.setOnFocusChangeListener(this);
        EditText destination = (EditText) view.findViewById(R.id.destination);
        destination.setOnFocusChangeListener(this);
        EditText price = (EditText) view.findViewById(R.id.price);
        price.setOnFocusChangeListener(this);

        setSpinners(view);
        showRequestOptions();
        setListHeight();
    }

    private void setSpinners(View view) {
        Spinner bagSpinner = (Spinner) view.findViewById(R.id.bag);
        String [] bagOptions = getResources().getStringArray(R.array.bag_options);
        SpinnerAdapter bagAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item,
                bagOptions);
        bagAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        bagSpinner.setOnItemSelectedListener(this);
        bagSpinner.setAdapter(bagAdapter);

        Spinner smokerSpinner = (Spinner) view.findViewById(R.id.smoker);
        String [] smokerOptions = getResources().getStringArray(R.array.smoker_options);
        SpinnerAdapter smokerAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item,
                smokerOptions);
        smokerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        smokerSpinner.setOnItemSelectedListener(this);
        smokerSpinner.setAdapter(smokerAdapter);

        Spinner passengersSpinner = (Spinner) view.findViewById(R.id.passengers);
        String [] passengersOptions = getResources().getStringArray(R.array.passengers_options);
        SpinnerAdapter passengersAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item,
                passengersOptions);
        passengersAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        passengersSpinner.setOnItemSelectedListener(this);
        passengersSpinner.setAdapter(passengersAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showError(String text){
        TextView error = (TextView) getActivity().findViewById(R.id.error);
        error.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ride_btn:
                showRideOptions();
                break;
            case R.id.request_btn:
                showRequestOptions();
                break;
            case R.id.date:
                setDate(v);
                break;
            case R.id.time_from:
                setTime(v);
                break;
            case R.id.time_until:
                setTime(v);
                break;
            case R.id.add_stop:
                showAddStopDialog();
                break;
        }
    }

    public int getStopPosition(RideStop rideStop){
        return stops.indexOf(rideStop);
    }

    public void onAddStop(RideStop rideStop){
        stops.add(rideStop);
        adapter.notifyDataSetChanged();
        present.addStop(rideStop);
        setListHeight();
    }

    public void onDeleteStop(int position){
        stops.remove(position);
        adapter.notifyDataSetChanged();
        present.deleteStop(position);
        setListHeight();
    }

    public void setListHeight(){
        ListView list = getListView();
        int height = 0;

        for (int i = 0; i < stops.size(); i++){
            View item = adapter.getView(i, null, list);
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = height + (list.getDividerHeight() * (adapter.getCount() - 1));
        list.setLayoutParams(params);
    }

    public void showAddStopDialog() {
        StopDialog stopDialog = new StopDialog();
        stopDialog.setTargetFragment(this, ADD_STOP_CODE);
        stopDialog.show(getFragmentManager(), "stopDialog");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_STOP_CODE){
            RideStop rideStop = new RideStop(data.getStringExtra("address"),
                    data.getStringExtra("price"), data.getStringExtra("time"));
            onAddStop(rideStop);
        }
        if(requestCode == DATE_CODE){
            int id = data.getIntExtra("id", -1);
            if(id == -1) return;
            present.changedValue(data.getStringExtra("text"), id);
        }
    }

    public void setTime(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", v.getId());
        timePickerFragment.setArguments(args);
        timePickerFragment.setTargetFragment(this, DATE_CODE);
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    public void setDate(View v) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", v.getId());
        datePickerFragment.setArguments(args);
        datePickerFragment.setTargetFragment(this, DATE_CODE);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void showRideOptions() {
        View rideBtn = getActivity().findViewById(R.id.ride_btn);
        View requestBtn = getActivity().findViewById(R.id.request_btn);
        rideBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_tab_active));
        requestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));

        showView(getActivity().findViewById(R.id.price));
        showView(getActivity().findViewById(R.id.nis));
        showView(getActivity().findViewById(R.id.add_stop));
        showView(getActivity().findViewById(R.id.passengers));
        showView(getActivity().findViewById(R.id.post_ride));
        hideView(getActivity().findViewById(R.id.post_request));
        hideView(getActivity().findViewById(R.id.manual_search));
        hideView(getActivity().findViewById(R.id.bag));
        showView(getListView());

        present.setRideMode();
    }

    private void showRequestOptions() {
        View rideBtn = getActivity().findViewById(R.id.ride_btn);
        View requestBtn = getActivity().findViewById(R.id.request_btn);
        rideBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
        requestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_tab_active));

        hideView(getActivity().findViewById(R.id.price));
        hideView(getActivity().findViewById(R.id.nis));
        hideView(getActivity().findViewById(R.id.add_stop));
        hideView(getActivity().findViewById(R.id.passengers));
        hideView(getActivity().findViewById(R.id.post_ride));
        showView(getActivity().findViewById(R.id.post_request));
        showView(getActivity().findViewById(R.id.manual_search));
        showView(getActivity().findViewById(R.id.bag));
        hideView(getListView());

        present.setRequestMode();
    }

    private void hideView(View v){
        v.setVisibility(View.GONE);
    }

    private void showView(View v){
        v.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus && v instanceof EditText && !((EditText) v).getText().toString().equals("")){
            present.changedValue(((EditText) v).getText().toString(), v.getId());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0 && view != null && view instanceof TextView) {
            present.changedValue(((TextView) view).getText().toString(), view.getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
