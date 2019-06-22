package com.lntuplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/notice")
public class NoticeController {

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get() {

        List<Map> list = (List<Map>) servletContext.getAttribute("notice");
        if (list == null) {
            return "查询教务公告失败!";
        }
        return list;
    }
}
