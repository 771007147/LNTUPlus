package com.lntuplus.controller;

import com.lntuplus.action.StuInfoAction;
import com.lntuplus.model.StuInfoModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/stuinfo")
public class StuInfoController {
    private static final Logger logger = LoggerFactory.getLogger(StuInfoController.class);

    //全局Context
    @Autowired
    private ServletContext servletContext;
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get(HttpServletRequest req) {
        String number = req.getParameter("number");
        String password = req.getParameter("password");
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
            return map;
        }
//        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        Map<String, Object> stuInfoMap = new StuInfoAction().get(port, session);
        if (!stuInfoMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            System.out.println(TimeUtils.getTime() + " getStuInfo失败：" + stuInfoMap.get(Constants.STRING_SUCCESS));
            map.put(Constants.STRING_SUCCESS, stuInfoMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        StuInfoModel stuInfoData = (StuInfoModel) stuInfoMap.get(Constants.STRING_DATA);
        stuInfoData.setPassword(password);
        map.put(Constants.STRING_DATA, stuInfoData);
        return map;
    }
}
