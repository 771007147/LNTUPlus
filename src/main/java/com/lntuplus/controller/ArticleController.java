package com.lntuplus.controller;

import com.lntuplus.action.ArticleAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get() {
        ArticleAction articleAction = new ArticleAction();
        Map<String, Object> map = articleAction.get();
        return map;
    }
}
