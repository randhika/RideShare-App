package com.rideshare.rideshare.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private final static int DATE_CODE = 101;

    private int textViewId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        textViewId = (int) getArguments().get("id");
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(1900, 0, 0, hourOfDay, minute);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        ((EditText) (getActivity().findViewById(textViewId))).setText(df.format(c.getTime()));

        Intent data = new Intent();
        data.putExtra("id", textViewId);
        data.putExtra("text", df.format(c.getTime()));

        getTargetFragment().onActivityResult(DATE_CODE, DATE_CODE, data);
    }
}