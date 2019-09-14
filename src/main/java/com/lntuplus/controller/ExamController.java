package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.AsyncAction;
import com.lntuplus.action.ExamAction;
import com.lntuplus.model.ExamModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.GsonUtils;
import com.lntuplus.utils.OkHttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/exam")
public class ExamController {

    //全局Context
    @Autowired
    private ServletContext servletContext;
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();
    @Autowired
    private AsyncAction mAsyncAction;

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get(HttpServletRequest req) {
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        System.out.println(number + " 开始获取考试...");
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        String port = (String) servletContext.getAttribute("port");

        Map<String, String> loginMap = mOkHttpUtils.login(number, password,port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            System.out.println(number + " 登录失败！");
            return gson.toJson(map);
        }
//        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        Map<String, Object> examMap = new ExamAction().get(port, session, number);
        if (!examMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, examMap.get(Constants.STRING_SUCCESS));
            System.out.println(number + " 获取考试失败！");
            return map;
        }
        List<ExamModel> examData = (List<ExamModel>) examMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_DATA, examData);
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
        System.out.println(number + " 获取考试成功！");
//        mAsyncAction.saveExam(examData);
        return map;
    }
}
