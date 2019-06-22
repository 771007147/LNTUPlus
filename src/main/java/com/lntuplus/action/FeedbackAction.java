package com.lntuplus.action;

import com.lntuplus.utils.ComBean;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.TimeUtils;

import java.util.HashMap;
import java.util.Map;


public class FeedbackAction {
    private String success = "true";

    public Map<String, String> post(String number, String text, String qq) {
        Map<String, String> map = new HashMap<>();
        ComBean cBean = new ComBean();
        int flag = cBean.comUp("insert into feedback values('" + number + "','" + qq + "','" + text + "','" + TimeUtils.getTime() + "','" + 0 + "');");
        if (flag != 1) {
            success = "saveError";
            map.put(Constants.STRING_SUCCESS, success);
            System.out.println(number + "反馈信息保存失败");
            return map;
        } else {
            success = "true";
            map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
            System.out.println("学号:" + number + ",QQ:" + qq + "反馈信息已保存");
            System.out.println(text);
            return map;
        }
    }
}
