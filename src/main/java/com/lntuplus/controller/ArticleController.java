package com.lntuplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.Map;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get() {
        Map<String,Object> map = (Map<String, Object>) servletContext.getAttribute("article");
        if (map == null) {
            return "查询Article信息失败!";
        }
        return map;
    }
}
