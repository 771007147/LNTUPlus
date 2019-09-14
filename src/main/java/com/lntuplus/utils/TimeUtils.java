package com.lntuplus.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    private static final int FIRST_MONDAY_DATE = 0;
    private static final int FIRST_MONDAY_DAY = 1;


    public static String getTime() {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(now);
    }

    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(now);
    }

    public static Date toDate(String s) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date toDateTime(String s) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String stampToDate(Long stamp) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static int weekNo() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Date marchMon = firstMonDate(year, 3);
        Date septMon = firstMonDate(year, 9);
        long day = 86400000L;
        long time;
        if (now.after(marchMon) && now.before(septMon)) {
            time = now.getTime() - marchMon.getTime();
        } else if (now.after(septMon)) {
            time = now.getTime() - septMon.getTime();
        } else {
            year -= 1;
            Date lastYear = firstMonDate(year, 9);
            time = now.getTime() - lastYear.getTime();
        }

        long week = time / day / 7;
        return (int) week;
    }

    private static Calendar firstMonday(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        int day = 1;
        cal.set(Calendar.DAY_OF_MONTH, day);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.set(Calendar.DAY_OF_MONTH, ++day);
        }
        Date date = cal.getTime();

        String dateS = new SimpleDateFormat("yyyy-MM-dd").format(date);
//        System.out.println(dateS);
        return cal;
    }

    public static Date firstMonDate(int year, int month) {
        Calendar calendar = firstMonday(year, month);
        Date date = calendar.getTime();
        return date;

    }

    public static int firstMonDay(int year, int month) {
        Calendar calendar = firstMonday(year, month);

        return calendar.get(Calendar.DAY_OF_MONTH);

    }

}
