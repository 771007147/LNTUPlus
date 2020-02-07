package com.lntuplus.controller;

import com.lntuplus.action.WeekAction;
import com.lntuplus.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/week")
public class WeekController {

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get() {
        return new WeekAction().getWeek();
    }
}
