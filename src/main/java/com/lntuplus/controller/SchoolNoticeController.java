package com.lntuplus.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lntuplus.model.SchoolNoticeModel;
import com.lntuplus.utils.JDBCUtils;
import com.lntuplus.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/schoolnotice")
public class SchoolNoticeController {

    @ResponseBody
    @RequestMapping(value = "/get")
    public String get(HttpServletRequest req, HttpServletResponse resp) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Map<String, Object> noticeHashcode = JDBCUtils.query("select hashcode from schoolnotice where pNo = '0' and rNo ='0' ;");
        if ((int) noticeHashcode.get("hashcode") == Integer.valueOf(req.getParameter("hashcode"))) {
            Map<String, String> map = new HashMap<>();
            map.put("success", "same");
            System.out.println(new TimeUtils().getTime() + ":查询教务公告完毕！");
            return gson.toJson(map);
        }

        SchoolNoticeModel schoolNoticeModel = new SchoolNoticeModel();
        String num = JDBCUtils.query("select count(*) from schoolnotice").get("count(*)").toString();
        if (Integer.valueOf(num) <= 0) {
            schoolNoticeModel.setSuccess("null");
            return gson.toJson(schoolNoticeModel);
        }
        List<List<Map>> noticeList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int page = i + 1;
            List<Map> pageList = new ArrayList<>();
            for (int j = 0; j < 13; j++) {
                int number = j + 1;
                Map<String, Object> item = JDBCUtils.query("select notices from schoolnotice where pNo = '" + page + "' and rNo = '" + number + "';");
                pageList.add(item);
            }
            noticeList.add(pageList);
        }
        System.out.println("请求教务公告成功！");
        return gson.toJson(noticeList);
    }
}
