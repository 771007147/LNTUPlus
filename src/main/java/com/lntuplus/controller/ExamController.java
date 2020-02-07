package com.lntuplus.controller;

import com.lntuplus.action.AsyncAction;
import com.lntuplus.action.ExamAction;
import com.lntuplus.model.ExamModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
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
        logger.info(number + " 开始获取考试...");
        Map<String, Object> map = new HashMap<>();
        String port = (String) servletContext.getAttribute("port");
        if (port.equals(Constants.STRING_ERROR)) {
            logger.info("Port error");
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
            return map;
        }
        Map<String, String> loginMap = mOkHttpUtils.login(number, password, port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            logger.error(number + " 登录失败！");
            return map;
        }
        String session = loginMap.get(Constants.STRING_SESSION);
        Map<String, Object> examMap = new ExamAction().get(port, session, number);
        if (!examMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, examMap.get(Constants.STRING_SUCCESS));
            logger.error(number + " 获取考试失败！");
            return map;
        }
        List<ExamModel> examData = (List<ExamModel>) examMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_DATA, examData);
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
        logger.info(number + " 获取考试成功！");
        mAsyncAction.saveExam(examData);
        return map;
    }
}
