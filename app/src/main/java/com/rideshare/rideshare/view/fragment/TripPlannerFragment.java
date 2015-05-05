package com.rideshare.rideshare.view.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.SpinnerAdapter;
import com.rideshare.rideshare.view.dialog.DatePickerFragment;
import com.rideshare.rideshare.view.dialog.TimePickerFragment;

public class TripPlannerFragment extends Fragment implements View.OnClickListener{

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trip_planner, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button rideBtn = (Button) view.findViewById(R.id.ride_btn);
        rideBtn.setOnClickListener(this);
        Button requestBtn = (Button) view.findViewById(R.id.request_btn);
        requestBtn.setOnClickListener(this);
        EditText date = (EditText) view.findViewById(R.id.date);
        date.setOnClickListener(this);
        EditText tf = (EditText) view.findViewById(R.id.time_from);
        tf.setOnClickListener(this);
        EditText tu = (EditText) view.findViewById(R.id.time_until);
        tu.setOnClickListener(this);
        showRequestOptions();
        setSpinners(view);
    }

    private void setSpinners(View view) {
        Spinner bagSpinner = (Spinner) view.findViewById(R.id.bag);
        String [] bagOptions = getResources().getStringArray(R.array.bag_options);
        SpinnerAdapter bagAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item,
                bagOptions);
        bagAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        bagSpinner.setAdapter(bagAdapter);

        Spinner smokerSpinner = (Spinner) view.findViewById(R.id.smoker);
        String [] smokerOptions = getResources().getStringArray(R.array.smoker_options);
        SpinnerAdapter smokerAdapter = new SpinnerAdapter(getActivity(), R.layout.spinner_item,
                smokerOptions);
        smokerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        smokerSpinner.setAdapter(smokerAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        }
    }

    public void setTime(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", v.getId());
        timePickerFragment.setArguments(args);
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    public void setDate(View v) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", v.getId());
        datePickerFragment.setArguments(args);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    private void showRideOptions() {
        View rideBtn = getActivity().findViewById(R.id.ride_btn);
        View requestBtn = getActivity().findViewById(R.id.request_btn);
        rideBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_tab_active));
        requestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
    }

    private void showRequestOptions() {
        View rideBtn = getActivity().findViewById(R.id.ride_btn);
        View requestBtn = getActivity().findViewById(R.id.request_btn);
        rideBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
        requestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_tab_active));
    }

    /*@Override
    public void onItemSelected(AdapterViewCompat<?> parent, View view, int position, long id) {
        if(position != 0){
            ((TextView) parent.getChildAt(position)).setTextColor(Color.BLUE);
        }
    }

    @Override
    public void onNothingSelected(AdapterViewCompat<?> parent) {

    }*/
}
