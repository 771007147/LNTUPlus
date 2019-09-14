package com.lntuplus.controller;

import com.lntuplus.model.EverydayModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;

@Controller
@RequestMapping(value = "/everyday")
public class EverydayController {
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/get")
    @ResponseBody
    public Object get() {
        EverydayModel everydayModel = (EverydayModel) servletContext.getAttribute("everyday");
        if (everydayModel == null) {
            return "查询Everyday信息失败!";
        }
        return everydayModel;
    }
}
