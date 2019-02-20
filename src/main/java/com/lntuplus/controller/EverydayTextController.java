package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.EverydayTextAction;
import com.lntuplus.model.EverydayTextModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/everydaytext")
public class EverydayTextController {

    @RequestMapping(value = "/get")
    @ResponseBody
    public String get() {
        EverydayTextAction everydayTextAction = new EverydayTextAction();
        Gson gson = new Gson();
        String everydayText = gson.toJson(everydayTextAction.get(), EverydayTextModel.class);
        return everydayText;
    }
}
