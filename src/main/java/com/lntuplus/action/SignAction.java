package com.lntuplus.action;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.lntuplus.model.SignModel;
import com.lntuplus.utils.ComBean;
import com.lntuplus.utils.DBSessionFactory;
import com.lntuplus.utils.JDBCUtils;
import com.lntuplus.utils.TimeUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.pkcs11.Secmod;

public class SignAction {

    private double latitude_GCJ02;
    private double longitude_GCJ02;
    private double latitude_WGS84;
    private double longitude_WGS84;

    public String sign(String number, String name, String iClass) {
        Gson gson = new Gson();
//        Gson
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int signCourseNo = courseNo(TimeUtils.getTime());
        Map<String, Object> map = new HashMap<>();
        if (signCourseNo == 0) {
            map.put("success", "timeError");
            System.out.println(TimeUtils.getTime() + ":当前非上课时间！");
            return gson.toJson(map);
        }
        SignModel signModel = new SignModel(Integer.valueOf(number), TimeUtils.getDate(), signCourseNo);
        int exist = sqlSession.selectOne("SignMapper.isExist", signModel);
        if (exist > 0) {
            map.put("success", "haveDone");
            System.out.println(number + " " + name + " " + iClass + " " + TimeUtils.getTime() + " 第" + signCourseNo + "节课已签到过！");
            return gson.toJson(map);
        }
        signModel.setDay(TimeUtils.getDate());
        signModel.setNo(signCourseNo);
        signModel.setSignTime(TimeUtils.getTime());
        sqlSession.insert("SignMapper.insert", signModel);
        System.out.println(number + " " + name + " " + iClass + " " + TimeUtils.getTime() + " 第" + signCourseNo + "节课签到成功！");
        map.put("sign", signModel);
        return gson.toJson(map);
    }

    private int courseNo(String date) {
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateTime = df.format(now);
        String[][] time = {
                {dateTime + " 08:00:00", dateTime + " 09:35:00"},
                {dateTime + " 09:55:00", dateTime + " 11:30:00"},
                {dateTime + " 13:30:00", dateTime + " 15:05:00"},
                {dateTime + " 15:25:00", dateTime + " 17:00:00"},
                {dateTime + " 18:30:00", dateTime + " 20:05:00"}
        };
        for (int i = 0; i < time.length; i++) {
            if (compare_date(date, time[i][0]) >= 0 && compare_date(date, time[i][1]) <= 0) {
                return i + 1;
            }
        }
        return 0;
    }

    private static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//				System.out.println("dt1 在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//				System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

}
