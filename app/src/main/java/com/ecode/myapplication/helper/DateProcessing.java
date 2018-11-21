package com.ecode.myapplication.helper;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateProcessing {
    static final Locale INDONESIA = new Locale("in", "ID");
    public static final SimpleDateFormat fDayMonthYear = new SimpleDateFormat("dd MMMM yyyy",INDONESIA);
    public static final SimpleDateFormat fHourMinute = new SimpleDateFormat("HH:mm",INDONESIA);
    public static final SimpleDateFormat fHourMinuteSecond = new SimpleDateFormat("HH:mm:ss",INDONESIA);
    public static final SimpleDateFormat fDayMonthYearHourMinute = new SimpleDateFormat("dd MMMM yyyy HH:mm",INDONESIA);

    static public String printDate(Long dateLong,Boolean isUTC,SimpleDateFormat simpleDateFormat){
        if(isUTC){
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return simpleDateFormat.format(new Date(dateLong));
    }
    static public long substractLongDate(Long newDatelong, Long oldDatelong){
        long diff =  newDatelong - oldDatelong;
        int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
        int hours = (int) (diff / (1000 * 60 * 60));
        int minutes = (int) (diff / (1000 * 60));
        int seconds = (int) (diff / (1000));
        Log.d("cal-numOfDays", String.valueOf(numOfDays));
        Log.d("cal-hours", String.valueOf(hours));
        Log.d("cal-minutes", String.valueOf(minutes));
        long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diff);
        Log.d("cal-newDatelong", String.valueOf(diff));
        Log.d("cal-newDatelong", printDate(diff,true,fHourMinute));
        return diff;
    }

}
