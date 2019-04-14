package com.lntuplus.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lntuplus.action.NoticeAction;
import com.lntuplus.model.NoticeModel;
import com.lntuplus.utils.DBSessionFactory;
import com.lntuplus.utils.TimeUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
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
@RequestMapping(value = "/notice")
public class NoticeController {

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/get")
    public Object get() {

        List<Map> list = (List<Map>) servletContext.getAttribute("notice");
        if (list == null) {
            return "查询教务公告失败!";
        }
        return list;


//        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        int count = sqlSession.selectOne("NoticeMapper.count_all_hashcode");
//        if (count > 0) {
//            int hashcode = sqlSession.selectOne("NoticeMapper.hashcode");
//            if (hashcode == Integer.valueOf(req.getParameter("hashcode"))) {
//                Map<String, String> map = new HashMap<>();
//                map.put("success", "same");
//                System.out.println(TimeUtils.getTime() + ":查询教务公告完毕,无需更新！");
//                return gson.toJson(map);
//            }
//        }
//        List noticeList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            int page = i + 1;
//            List pageList = new ArrayList<>();
//            for (int j = 0; j < 13; j++) {
//                int number = j + 1;
//                NoticeModel loca = new NoticeModel();
//                loca.setpNo(page);
//                loca.setrNo(number);
//                NoticeModel noticeModel = sqlSession.selectOne("NoticeMapper.select_notices", loca);
//                pageList.add(noticeModel);
//            }
//            noticeList.add(pageList);
//        }
//        sqlSession.close();
//        System.out.println(TimeUtils.getTime() +"请求教务公告成功！");
//        String string = gson.toJson(noticeList);
////        处理gson to json 对字符串的处理，转化为json
//        string = string.replaceAll("\\\\", "");
//        string = string.replaceAll("notices\":\"", "notices\":");
//        string = string.replaceAll("}\",\"datetime", "},\"datetime");
//        sqlSession.close();
//        return string;
    }
}
