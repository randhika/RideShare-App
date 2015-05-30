package com.rideshare.rideshare.utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

    public static int timeDiff(String time1, String time2){
        int seconds1 = secondsDay(time1);
        int seconds2 = secondsDay(time2);
        return seconds1 - seconds2;
    }

    public static int secondsDay(String time){
        String[] params = time.split(":");
        int hours = Integer.parseInt(params[0]) * 3600;
        int minuets = Integer.parseInt(params[1]) * 60;
        return hours + minuets;
    }

    public static String getDate(String ISODate) throws ParseException {
        DateFormat ISOFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.getDefault());
        DateFormat viewFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm",
                Locale.getDefault());
        Date date = ISOFormat.parse(ISODate);
        return viewFormat.format(date);
    }
}


