package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.HelloAction;
import com.lntuplus.model.HelloModel;
import com.lntuplus.utils.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/hello")
public class HelloController {

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object hello() {
        Gson gson = GsonUtils.getInstance();
        List<HelloModel> list = new HelloAction().get();
        int random = (int) (Math.random() * list.size());
        return list.get(random);
    }
}
