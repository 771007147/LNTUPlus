package com.lntuplus.action;

import com.lntuplus.utils.TimeUtils;

public class WeekAction {
    public int getWeek() {
        int week = TimeUtils.weekNo();
        if (week == 25) {
            return 0;
        } else {
            return week + 1;
        }
    }
}
