package com.rideshare.rideshare.utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateHandler{

    public DateHandler(){
    }

    public long fromIsoToEpoch(String isoDate) throws ParseException{

        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(isoDate);
        return date.getTime();
    }

    public String fromEpochToIso(String epochDate) throws ParseException{

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        String formatted = format.format(new Date(Long.parseLong(epochDate)));
        return formatted;
    }

    public String fromEpochToIso(long epochDate) throws ParseException{

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        String formatted = format.format(new Date(epochDate));
        return formatted;
    }

    public String fromEpochToIsoTimeZone0(long epochDate) throws ParseException{

        DateFormat format = new SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formatted = format.format(new Date(epochDate));
        return formatted;
    }

    public String timeDifference(String iso1, String iso2) throws ParseException{

        long epocFormat1 = fromIsoToEpoch(iso1);
        epocFormat1 -= fromIsoToEpoch(iso2);
        return fromEpochToIsoTimeZone0(epocFormat1);
    }
}


