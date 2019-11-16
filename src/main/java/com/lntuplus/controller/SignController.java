package com.lntuplus.controller;

import com.lntuplus.action.SignAction;
import com.lntuplus.model.SignGetModel;
import com.lntuplus.model.SignModel;
import com.lntuplus.utils.DBSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/sign")
@CrossOrigin
public class SignController {

    private static final Logger logger = LoggerFactory.getLogger(SignController.class);

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

//    @RequestMapping(value = "/query")
//    public String sign() {
//        return "sign";
//    }

    @RequestMapping(value = "/get")
    @ResponseBody
    public Object get(SignGetModel signGetModel) {
        String iClass = signGetModel.getiClass();
        String date = signGetModel.getDate();
        int index = signGetModel.getIndex();
        Map<String, Object> signMap = new HashMap<>();
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SignModel signModel = new SignModel();
        signModel.setiClass(iClass);
        signModel.setDate(date);
        signModel.setIndex(index);
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
        signMap.put("list", back);
        signMap.put("sum", all.size());
        System.out.println("查询签到：");
        System.out.println("班级：" + iClass + " 日期：" + date + " 课序：" + index);
        return signMap;
    }

}
