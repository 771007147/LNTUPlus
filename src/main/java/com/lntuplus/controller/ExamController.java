package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.ExamAction;
import com.lntuplus.action.LoginAction;
import com.lntuplus.model.ExamModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.GsonUtils;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/exam")
public class ExamController {
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();

    @ResponseBody
    @RequestMapping(value = "/get")
    public String get(HttpServletRequest req) {
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        Map<String, String> loginMap = mOkHttpUtils.login(number, password);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        Map<String, Object> examMap = new ExamAction().get(port, session, number);
        if (!examMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, examMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
        List<ExamModel> examData = (List<ExamModel>) examMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_DATA, examData);
        return gson.toJson(examData);
    }
}
