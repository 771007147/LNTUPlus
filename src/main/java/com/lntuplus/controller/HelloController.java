package com.lntuplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/hello")
public class HelloController {

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object hello() {
        List<Map> list = (List<Map>) servletContext.getAttribute("hello");
        if (list == null) {
            return "查询Hello信息失败!";
        }
        int random = (int) (Math.random() * list.size());
        return list.get(random);
    }
}
