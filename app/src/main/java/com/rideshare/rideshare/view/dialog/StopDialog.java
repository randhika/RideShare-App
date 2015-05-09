package com.rideshare.rideshare.view.dialog;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rideshare.rideshare.R;
import com.rideshare.rideshare.adapter.GooglePlacesAutocompleteAdapter;


public class StopDialog extends DialogFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener{

    private final static int ADD_STOP_CODE = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_stop_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addStopBtn = (Button) view.findViewById(R.id.stop_dialog_add);
        addStopBtn.setOnClickListener(this);
        Button cancelBtn = (Button) view.findViewById(R.id.stop_dialog_cancel);
        cancelBtn.setOnClickListener(this);
        MaterialAutoCompleteTextView stopAddress =
                (MaterialAutoCompleteTextView) view.findViewById(R.id.stop_dialog_address);
        stopAddress.setOnItemClickListener(this);
        setAutoComplete(view);
    }

    private void setAutoComplete(View view) {
        MaterialAutoCompleteTextView stopOverAddressView = (MaterialAutoCompleteTextView)
                view.findViewById(R.id.stop_dialog_address);
        stopOverAddressView.setAdapter(
                new GooglePlacesAutocompleteAdapter(view.getContext(), R.layout.autocomplete_item));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stop_dialog_add:
                onAddStop();
                break;
            case R.id.stop_dialog_cancel:
                dismiss();
                break;
        }
    }

    public void onAddStop(){
        String address = ((MaterialAutoCompleteTextView) getDialog().findViewById(R.id.stop_dialog_address))
                .getText().toString();
        String time = ((EditText) getDialog().findViewById(R.id.stop_dialog_time))
                .getText().toString();
        String price = ((EditText) getDialog().findViewById(R.id.stop_dialog_price))
                .getText().toString();

        if(address.equals("") || time.equals("") || price.equals("")){
            showError("Please Fill all the Fields");
            return;
        }

        Intent data = new Intent();
        data.putExtra("address", address);
        data.putExtra("time", time);
        data.putExtra("price", price);
        getTargetFragment().onActivityResult(ADD_STOP_CODE, ADD_STOP_CODE, data);
        dismiss();
    }

    public void showError(String text){
        TextView error = (TextView) getDialog().findViewById(R.id.error);
        error.setText(text);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String address;
        MaterialAutoCompleteTextView stopAddress =
                (MaterialAutoCompleteTextView) getDialog().findViewById(R.id.stop_dialog_address);
        address = (String) parent.getItemAtPosition(position);
        stopAddress.setText(address);
    }
}
