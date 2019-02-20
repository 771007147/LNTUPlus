package com.lntuplus.controller;

import com.lntuplus.action.HelloAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/hello")
public class HelloController {

    @ResponseBody
    @RequestMapping(value = "/get")
    public String hello() {
        String data = new HelloAction().get();
        return data;
    }
}
