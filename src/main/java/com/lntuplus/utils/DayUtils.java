package com.lntuplus.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayUtils {
    public static String firstMonday(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        int day = 1;
        cal.set(5, day);
        while (cal.get(7) != 2) {
            cal.set(5, ++day);
        }
        Date firstMonday = cal.getTime();
        String dtStr = new SimpleDateFormat("yyyy-MM-dd").format(firstMonday);
        System.out.println(dtStr);
        return dtStr;
    }

    public static void lastMonday(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, 1);
        int lastDay = cal.getActualMaximum(5);
        cal.set(5, lastDay);
        while (cal.get(7) != 2) {
            cal.set(5, --lastDay);
        }
        Date lastMonday = cal.getTime();
        String dtStr = new SimpleDateFormat("yyyy-MM-dd").format(lastMonday);
        System.out.println(dtStr);
    }

    public static int DayofFirstDay(int year, int month) {
        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, 1);
        int day = cal.get(7) - 1;
        if (day < 0) {
            day = 0;
        }
        System.out.println("星期" + weekDays[day]);
        return weekDays[day];
    }
}
