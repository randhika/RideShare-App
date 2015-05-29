package com.rideshare.rideshare.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.GooglePlacesAutocompleteAdapter;
import com.rideshare.rideshare.adapter.SpinnerAdapter;
import com.rideshare.rideshare.entity.app.Trip;
import com.rideshare.rideshare.present.RequestPresent;
import com.rideshare.rideshare.view.activity.NavigationActivity;
import com.rideshare.rideshare.view.dialog.DatePickerFragment;
import com.rideshare.rideshare.view.dialog.TimePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener{

    private Trip trip;
    private RequestPresent present;

    public static RequestFragment newInstance(Bundle args) {
        RequestFragment fragment = new RequestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        try{
            String tripStr = bundle.getString("TRIP");
            trip = Trip.fromJSON(new JSONObject(tripStr));
            present = new RequestPresent(this, trip);
        } catch (JSONException e) {
            Log.e("onCreateView", "JSONException", e);
        }

        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAutoComplete();
        setSpinners(view);

        EditText date = (EditText) view.findViewById(R.id.date);
        date.setOnClickListener(this);
        EditText tf = (EditText) view.findViewById(R.id.time_from);
        tf.setOnClickListener(this);
        EditText tu = (EditText) view.findViewById(R.id.time_until);
        tu.setOnClickListener(this);
        Button updateBtn = (Button) view.findViewById(R.id.update_request);
        updateBtn.setOnClickListener(this);

        ((EditText) getActivity().findViewById(R.id.source)).setText(trip.getSource());
        ((EditText) getActivity().findViewById(R.id.destination)).setText(trip.getDestination());
        ((EditText) getActivity().findViewById(R.id.date)).setText(trip.getDate());
        ((EditText) getActivity().findViewById(R.id.time_from)).setText(trip.getTimeFrom());
        ((EditText) getActivity().findViewById(R.id.time_until)).setText(trip.getTimeUntil());
    }

    private void setAutoComplete() {
        MaterialAutoCompleteTextView sourceView = (MaterialAutoCompleteTextView) getActivity()
                .findViewById(R.id.source);
        sourceView.setAdapter(
                new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.autocomplete_item, R.id.item));
        MaterialAutoCompleteTextView destinationView = (MaterialAutoCompleteTextView) getActivity()
                .findViewById(R.id.destination);
        destinationView.setAdapter(
                new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.autocomplete_item, R.id.item));
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

        switch (trip.getBagValue()){
            case(1): bagSpinner.setSelection(1); break;
            case(2): bagSpinner.setSelection(2); break;
        }
        switch (trip.getSmokerValue()) {
            case(1): smokerSpinner.setSelection(2); break;
            case(2): smokerSpinner.setSelection(3); break;
            case(3): smokerSpinner.setSelection(1); break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_request:
                updateRequest();
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

    private void updateRequest() {
        present.updateRequest();
    }

    public void setTime(View v) {
        DialogFragment timePickerFragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", v.getId());
        timePickerFragment.setArguments(args);
        timePickerFragment.setTargetFragment(this, 0);
        timePickerFragment.show(getFragmentManager(), "timePicker");
    }

    public void setDate(View v) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putInt("id", v.getId());
        datePickerFragment.setArguments(args);
        datePickerFragment.setTargetFragment(this, 0);
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    public String getTimeFrom(){
        return ((EditText) getActivity().findViewById(R.id.time_from)).getText().toString();
    }

    public String getDate(){
        return ((EditText) getActivity().findViewById(R.id.date)).getText().toString();
    }

    public String getTimeUntil(){
        return ((EditText) getActivity().findViewById(R.id.time_until)).getText().toString();
    }

    public String getSource(){
        return ((EditText) getActivity().findViewById(R.id.source)).getText().toString();
    }

    public String getDestination(){
        return ((EditText) getActivity().findViewById(R.id.destination)).getText().toString();
    }

    public int getSmoker(){
        Spinner spinner = ((Spinner)getActivity().findViewById(R.id.smoker));
        return spinner.getSelectedItemPosition();
    }

    public int getBags(){
        Spinner spinner = ((Spinner)getActivity().findViewById(R.id.bag));
        return spinner.getSelectedItemPosition();
    }

    public void error(String text) {
        TextView error = (TextView) getActivity().findViewById(R.id.error);
        error.setText(text);
    }

    public void clean(String id) {
        if(id.equals("source")){
            ((EditText) getActivity().findViewById(R.id.source)).setText("");
        } else {
            ((EditText) getActivity().findViewById(R.id.destination)).setText("");
        }
    }

    public void toMyRides() {
        ((NavigationActivity)getActivity()).selectItem(2, null);
    }
}
