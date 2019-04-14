package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.LoginAction;
import com.lntuplus.action.ScoreAction;
import com.lntuplus.action.TableAction;
import com.lntuplus.model.TableModel;
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
@RequestMapping(value = "/table")
public class TableController {
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
        Map<String, Object> tableMap = new TableAction().get(port, session);
        if (!tableMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, tableMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
        List<List<List<TableModel>>> tableData = (List<List<List<TableModel>>>) tableMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_DATA, tableData);
        return gson.toJson(map);
    }
}
