package com.lntuplus.controller;

import com.lntuplus.action.SignAction;
import com.lntuplus.model.SignModel;
import com.lntuplus.utils.ComBean;
import com.lntuplus.utils.DBSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/sign")
public class SignController {

    @ResponseBody
    @RequestMapping(value = "/sign")
    public Object sign(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json; charset=utf-8");
        SignAction signAction = new SignAction();
        String number = req.getParameter("number");
        String name = req.getParameter("name");
        String iClass = req.getParameter("iClass");
        Map map = signAction.sign(number, name, iClass);
        return map;
    }

    @RequestMapping(value = "/query")
    public String sign() {
        return "sign";
    }

    @RequestMapping(value = "/get")
    public String get(HttpServletRequest req, ModelMap model) throws UnsupportedEncodingException {
        req.setCharacterEncoding("UTF-8");
        String iClass = req.getParameter("iClass");
        String day = req.getParameter("day");
        String no = req.getParameter("no");
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SignModel signModel = new SignModel();
        signModel.setiClass(iClass);
        signModel.setDay(day);
        signModel.setNo(Integer.valueOf(no));
        List<SignModel> list = sqlSession.selectList("SignMapper.selectSigned", signModel);
        List<SignModel> all = sqlSession.selectList("SignMapper.selectClass", signModel);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(String.valueOf(list.get(i).getNumber()), "ok");
        }
        List<SignModel> back = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (!map.containsKey(String.valueOf(all.get(i).getNumber()))) {
                back.add(all.get(i));
            }
        }
        model.addAttribute("list", back);
        model.addAttribute("number", back.size());
        System.out.println("查询签到：");
        System.out.println("班级：" + iClass + " 日期：" + day + " 课序：" + no);
        return "sign";
    }
}
