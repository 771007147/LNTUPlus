package com.lntuplus.action;

import com.google.gson.Gson;
import com.lntuplus.model.SignModel;
import com.lntuplus.utils.DBSessionFactory;
import com.lntuplus.utils.GsonUtils;
import com.lntuplus.utils.TimeUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignAction {

    private double latitude_GCJ02;
    private double longitude_GCJ02;
    private double latitude_WGS84;
    private double longitude_WGS84;

    public Map sign(String number, String name, String iClass) {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int signCourseNo = courseNo(TimeUtils.getTime());
        Map<String, Object> map = new HashMap<>();
        if (signCourseNo == 0) {
            map.put("success", "timeError");
            System.out.println(TimeUtils.getTime() + " 当前非上课时间！");
            return map;
        }
        SignModel signModel = new SignModel(Integer.valueOf(number), TimeUtils.getDate(), signCourseNo);
        int exist = sqlSession.selectOne("SignMapper.isExist", signModel);
        if (exist > 0) {
            map.put("success", "haveDone");
            System.out.println(number + " " + name + " " + iClass + " " + TimeUtils.getTime() + " 第" + signCourseNo + "节课已签到过！");
            sqlSession.close();
            return map;
        }
        signModel.setDay(TimeUtils.getDate());
        signModel.setNo(signCourseNo);
        signModel.setSignTime(TimeUtils.getTime());
        signModel.setName(name);
        signModel.setiClass(iClass);
        sqlSession.insert("SignMapper.insert", signModel);
        sqlSession.commit();
        System.out.println(number + " " + name + " " + iClass + " " + TimeUtils.getTime() + " 第" + signCourseNo + "节课签到成功！");
        map.put("success", "success");
        map.put("sign", signModel);
        sqlSession.close();
        return map;
    }

    private int courseNo(String date) {
        String dateTime = TimeUtils.getDate();
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
