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
@RequestMapping(value = "/timeline")
public class TimelineController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/sendMessage")
    public Object sendMessage(HttpServletRequest req, HttpServletResponse resp) {
        String number = req.getParameter("number");
        int type = Integer.parseInt(req.getParameter("type"));
        String name = req.getParameter("name");
        String text = req.getParameter("text");
        String imgUrl = req.getParameter("imgUrl");
        String id = System.currentTimeMillis() + number;
        String datetime = String.valueOf(System.currentTimeMillis());
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int flag = sqlSession.insert("TimelineMapper.insertMessage", new TimelineMessageModel(id, number, type, name, text, imgUrl, datetime));
        sqlSession.commit();
        int code;
        String msg;
        if (flag > 0) {
            logger.info("保存Timeline message成功");
            code = 10000;
            msg = "发送成功！";
        } else {
            logger.info("保存Timeline message失败");
            code = 40000;
            msg = "发送失败，请重试！";
        }
        sqlSession.close();
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("msg", msg);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/getMessages")
    public Object getMessage() {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        List<TimelineMessageModel> messages = sqlSession.selectList("TimelineMapper.getMessages");
        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            Map<String, Object> messageItem = new HashMap<>();
            messageItem.put("id", messages.get(i).getId());
            messageItem.put("number", messages.get(i).getType());
            messageItem.put("type", messages.get(i).getType());
            messageItem.put("name", messages.get(i).getName());
            messageItem.put("text", messages.get(i).getText());
            messageItem.put("imgUrl", messages.get(i).getImgUrl());
            messageItem.put("datetime", messages.get(i).getDatetime());
            Map<String, String> select = new HashMap<>();
            select.put("preid", messages.get(i).getId());
            List<TimelineReplyModel> replys = sqlSession.selectList("TimelineMapper.getReplys", select);
            messageItem.put("replys", replys);
            data.add(messageItem);
        }
        sqlSession.close();

        int code;
        String msg;
        code = 10000;
        msg = "刷新成功！";
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("msg", msg);
        response.put("data", data);
        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/replyMessage")
    public Object replyMessage(HttpServletRequest req, HttpServletResponse resp) {
        String preid = req.getParameter("preid");
        String number = req.getParameter("number");
        String id = System.currentTimeMillis() + number;
        String name = req.getParameter("name");
        String text = req.getParameter("text");
        String datetime = String.valueOf(System.currentTimeMillis());
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int flag = sqlSession.insert("TimelineMapper.insertReply", new TimelineReplyModel(preid, id, number, name, text, datetime));
        sqlSession.commit();
        int code;
        String msg;
        if (flag > 0) {
            logger.info("保存Timeline reply成功");
            code = 10000;
            msg = "回复成功！";
        } else {
            logger.info("保存Timeline reply失败");
            code = 40000;
            msg = "回复失败，请重试！";
        }
        sqlSession.close();
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("msg", msg);
        return response;
    }


}
