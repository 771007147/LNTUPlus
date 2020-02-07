package com.lntuplus.controller;

import com.lntuplus.action.AsyncAction;
import com.lntuplus.action.ScoreAction;
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
@RequestMapping(value = "/score")
public class ScoreController {

    private static final Logger logger = LoggerFactory.getLogger(ScoreController.class);
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
        Map<String, Object> map = new HashMap<>();
        logger.info(number + " 开始获取成绩...");
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
        Map<String, Object> scoreMap = new ScoreAction().get(port, session, number);
        if (!scoreMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, scoreMap.get(Constants.STRING_SUCCESS));
            logger.error(number + " 获取成绩失败！");
            return map;
        }
        List<Map<String, Object>> scoreData = (List<Map<String, Object>>) scoreMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_DATA, scoreData);
        map.put(Constants.STRING_GPA, scoreMap.get(Constants.STRING_GPA));
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
        logger.info(number + " 获取成绩成功！");
        mAsyncAction.saveScore(scoreData, number, (double) scoreMap.get(Constants.STRING_GPA));
        return map;
    }
}
