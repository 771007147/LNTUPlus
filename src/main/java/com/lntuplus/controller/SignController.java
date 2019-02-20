package com.lntuplus.controller;

import com.lntuplus.action.SignAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/sign")
public class SignController {

    @ResponseBody
    @RequestMapping({"/sign"})
    public String sign(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=utf-8");
        SignAction signAction = new SignAction();
        String number = req.getParameter("number");
        String name = req.getParameter("name");
        String iClass = req.getParameter("iClass");
        String data = signAction.sign(number, name, iClass);
        return data;
    }
}
