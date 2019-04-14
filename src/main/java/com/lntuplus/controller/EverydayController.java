package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.EverydayAction;
import com.lntuplus.model.EverydayModel;
import com.lntuplus.utils.GsonUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/everyday")
public class EverydayController {

    @RequestMapping(value = "/get")
    @ResponseBody
    public Object get() {
        EverydayAction everydayAction = new EverydayAction();
        Gson gson = GsonUtils.getInstance();
        EverydayModel everydayModel = everydayAction.get();
        return everydayModel;
    }
}
