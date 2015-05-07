package com.rideshare.rideshare.view.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private final static int DATE_CODE = 101;

    private int textViewId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        textViewId = (int) getArguments().get("id");
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ((EditText) (getActivity().findViewById(textViewId))).setText(df.format(c.getTime()));

        Intent data = new Intent();
        data.putExtra("id", textViewId);
        data.putExtra("text", df.format(c.getTime()));

        getTargetFragment().onActivityResult(DATE_CODE, DATE_CODE, data);
    }
}