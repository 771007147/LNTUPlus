package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.TableAction;
import com.lntuplus.model.TableModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.GsonUtils;
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
@RequestMapping(value = "/table")
public class TableController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
    @Autowired
    private ServletContext servletContext;
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get(HttpServletRequest req) {
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        Map<String, Object> map = new HashMap<>();
        logger.info(number + " 开始获取课表...");
        String port = (String) servletContext.getAttribute("port");
        Map<String, String> loginMap = mOkHttpUtils.login(number, password,port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            logger.error(number + " 登录失败！");
            return map;
        }
//        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        Map<String, Object> tableMap = new TableAction().get(port, session);
        if (!tableMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, tableMap.get(Constants.STRING_SUCCESS));
            logger.error(number + " 获取课表失败！");
            return map;
        }
        List<List<List<TableModel>>> tableData = (List<List<List<TableModel>>>) tableMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_DATA, tableData);
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
        logger.info(number + " 获取课表成功！");
        return map;
    }
}
