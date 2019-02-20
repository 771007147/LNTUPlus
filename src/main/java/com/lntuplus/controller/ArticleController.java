package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.ArticleAction;
import com.lntuplus.model.ArticleModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @ResponseBody
    @RequestMapping(value = "/get")
    private String get() {
        Gson gson = new Gson();
        ArticleAction articleAction = new ArticleAction();
        ArticleModel articleModel = articleAction.get();
        return gson.toJson(articleModel);
    }
}
