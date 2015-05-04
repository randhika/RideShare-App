package com.rideshare.rideshare.entity.app;

import android.text.format.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Notification {

    private String header;
    private String body;
    private String created;

    public String getHeader(){
        return header;
    }

    public String getBody(){
        return body;
    }

    public String getDate(Locale locale) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
        Date date = dateFormat.parse(created);
        return (String) DateFormat.format("d MMM yyyy", date);
    }
}
