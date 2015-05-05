package com.rideshare.rideshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rideshare.rideshare.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private String[] options;
    private int resource;
    private int dropdownResource;

    public SpinnerAdapter(Context context, int resource, String[] options) {
        super(context, resource, options);
        this.options = options;
        this.resource = resource;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            TextView textView = new TextView(getContext());
            textView.setHeight(0);
            textView.setVisibility(View.GONE);
            return textView;
        }
        return getSpinnerView(position, convertView, parent, dropdownResource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getSpinnerView(position, convertView, parent, this.resource);
    }

    public View getSpinnerView(int position, View convertView, ViewGroup parent, int resource){
        convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        TextView textView = (TextView) convertView.findViewById(R.id.text_spinner);
        textView.setText(options[position]);
        if(position == 0){
            int grayColor = getContext().getResources().getColor(R.color.gray);
            textView.setTextColor(grayColor);
        }
        return textView;
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
        this.dropdownResource = resource;
    }
}
