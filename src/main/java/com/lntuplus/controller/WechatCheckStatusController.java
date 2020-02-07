package com.lntuplus.controller;

import com.lntuplus.model.TimelineMessageModel;
import com.lntuplus.model.TimelineReplyModel;
import com.lntuplus.model.WechatCheckShowStatusModel;
import com.lntuplus.utils.DBSessionFactory;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wechatCheck")
public class WechatCheckStatusController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/getShowStatus")
    public Object getShowStatus() {
        List<WechatCheckShowStatusModel> list = (List<WechatCheckShowStatusModel>) servletContext.getAttribute("wechatCheckShowStatus");
        int code = 10000;
        String msg = "获取审核View状态成功！";
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("msg", msg);
        response.put("data", list);
        return response;
    }
}
