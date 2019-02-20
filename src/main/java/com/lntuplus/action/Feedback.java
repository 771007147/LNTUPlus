package com.lntuplus.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lntuplus.utils.ComBean;
import com.lntuplus.utils.TimeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({"/Feedback"})
public class Feedback {
    private String success = "true";
    private Map response = new HashMap();

    @ResponseBody
    @RequestMapping({"/post"})
    public Object post(HttpServletRequest req, HttpServletResponse resp) {
        String number = req.getParameter("number");
        String text = req.getParameter("text");
        String qq = req.getParameter("qq");
        ComBean cBean = new ComBean();
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = df.format(now);
        int flag = cBean.comUp("insert into feedback values('" + number + "','" + qq + "','" + text + "','" + new TimeUtils().getTime() + "','" + 0 + "');");
        if (flag != 1) {
            success = "saveError";
            response.put("success", success);
            System.out.println(number + "反馈信息保存失败");
            return response;
        } else {
            success = "true";
            response.put("success", success);
            System.out.println("学号:" + number + ",QQ:" + qq + "反馈信息已保存");
            System.out.println(text);
            return response;
        }
    }
}
