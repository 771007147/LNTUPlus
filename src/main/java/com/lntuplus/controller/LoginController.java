package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.AsyncAction;
import com.lntuplus.model.ExamModel;
import com.lntuplus.model.StuInfoModel;
import com.lntuplus.model.TableModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.GsonUtils;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.Future;

@Controller
@RequestMapping(value = "/login")
@EnableAsync
public class LoginController {

    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();

    @Autowired
    private AsyncAction mAsyncAction;

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/get")
    @SuppressWarnings("unchecked")
    public Object get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setContentType("application/json; charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        System.out.println("学号：" + number + "登录中...");
        Map<String, String> loginMap = mOkHttpUtils.login(number, password);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);

        Future<Map<String, Object>> stuInfoFuture = mAsyncAction.getStuInfo(port, session, password);
        Future<Map<String, Object>> scoreFuture = mAsyncAction.getScore(port, session, number);
        Future<Map<String, Object>> examFuture = mAsyncAction.getExam(port, session, number);
        Future<Map<String, Object>> tableFuture = mAsyncAction.getTable(port, session);

        while (true) {
            if (stuInfoFuture.isDone() && scoreFuture.isDone() && examFuture.isDone() && tableFuture.isDone()) {
                break;
            }
            Thread.sleep(50);
        }
        Map<String, Object> stuInfoMap = stuInfoFuture.get();
        if (!stuInfoMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            System.out.println(TimeUtils.getTime() + " getStuInfo失败：" + stuInfoMap.get(Constants.STRING_SUCCESS));
            map.put(Constants.STRING_SUCCESS, stuInfoMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        StuInfoModel stuInfoData = (StuInfoModel) stuInfoMap.get(Constants.STRING_DATA);
        stuInfoData.setPassword(password);
        map.put(Constants.STRING_STU_INFO, stuInfoData);

        Map<String, Object> scoreMap = scoreFuture.get();
        if (!scoreMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, scoreMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        List<Map<String, Object>> scoreData = (List<Map<String, Object>>) scoreMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_SCORE, scoreData);
        map.put(Constants.STRING_GPA, scoreMap.get(Constants.STRING_GPA));
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);

        Map<String, Object> examMap = examFuture.get();
        if (!examMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, examMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        List<ExamModel> examData = (List<ExamModel>) examMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_EXAM, examData);

        Map<String, Object> tableMap = tableFuture.get();
        if (!tableMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, tableMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        List<List<List<TableModel>>> tableData = (List<List<List<TableModel>>>) tableMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_TABLE, tableData);
//        reflectStuInfo(stuInfoData);

        System.out.println("登陆成功！学号：" + number);
        return map;
    }

}
