package com.lntuplus.controller;

import com.lntuplus.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/time")
public class TimeController {

    @ResponseBody
    @RequestMapping(value = "/week")
    public String week() {
        return String.valueOf(TimeUtils.weekNo());
    }


}
