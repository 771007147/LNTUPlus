package com.lntuplus.controller;

import com.lntuplus.action.FeedbackAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping({"/feedback"})
public class FeedbackController {

    @ResponseBody
    @RequestMapping({"/post"})
    public Object post(HttpServletRequest req) {
        String number = req.getParameter("number");
        String text = req.getParameter("text");
        String qq = req.getParameter("qq");
        Map<String, String> map = new FeedbackAction().post(number, text, qq);
        return map;
    }
}
