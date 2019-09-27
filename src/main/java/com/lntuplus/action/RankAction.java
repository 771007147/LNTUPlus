package com.lntuplus.action;

import com.lntuplus.model.GPAModel;
import com.lntuplus.utils.DBSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankAction {


    private SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
    private SqlSession sqlSession = sqlSessionFactory.openSession();

    public Map<String, Integer> gpaModeChoose(String gpa, String number, int mode) {
        switch (mode) {
            case 0:
                return null;
            case 1:
                return null;
            default:
                return null;
        }
    }

    //    mode: 0 专业排名，1 历史排名
    public Map<String, Integer> scoreModeChoose(String course, String mark, String number, int mode) {
        switch (mode) {
            case 0:
                return singleMarkSplit(course, mark, number);
            case 1:
                return histroyMarkSplit(course, mark, number);
            default:
                return singleMarkSplit(course, mark, number);
        }
    }

    public Map<String, Integer> singleMarkSplit(String course, String mark, String number) {
        Map<String, Integer> map = new HashMap<>();
        switch (mark) {
            case "合格":
            case "不合格":
                return twoPoint(course, mark);
            case "优秀":
            case "良":
            case "中":
            case "及格":
            case "不及格":
                return fivePoint(course, mark);
            case "":
                return spaceMark(course);
            default:
                return hundredPoint(course, mark);

        }
    }

    private Map<String, Integer> twoPoint(String course, String mark) {
        return null;
    }

    private Map<String, Integer> fivePoint(String course, String mark) {
        return null;
    }

    private Map<String, Integer> hundredPoint(String course, String mark) {
        Map<String, String> map = new HashMap<>();
        map.put("course", course);
        map.put("mark", mark);
        int num = sqlSession.selectOne("RankMapper.singleProHundred", mark);
        System.out.println("number:" + num);
        return null;
    }

    private Map<String, Integer> spaceMark(String course) {
        return null;
    }

    public void closeSqlSession() {
        sqlSession.close();
    }

    private Map<String, Integer> histroyMarkSplit(String course, String mark, String number) {
        Map<String, Integer> map = new HashMap<>();
        switch (mark) {
            case "合格":
            case "不合格":
                return twoPoint(course, mark);
            case "优秀":
            case "良":
            case "中":
            case "及格":
            case "不及格":
                return fivePoint(course, mark);
            case "":
                return spaceMark(course);
            default:
                return hundredPoint(course, mark);

        }
    }

    public Map<String, Integer> GPARank(String number, String gpa, int mode) {
        String numb = null;
        switch (mode) {  //0：班级排名 1:专业排名 2：学院排名 3：全校排名
            case 0:
                numb = number.substring(0, 8) + "%";
                break;
            case 1:
                numb = number.substring(0, 6) + "%";
                break;
            case 2:
                numb = number.substring(0, 4) + "%";
                break;
            case 3:
                numb = number.substring(0, 2) + "%";
        }
        Map<String, String> map = new HashMap<>();
        map.put("number", numb);
        map.put("gpa", gpa);
        int num = sqlSession.selectOne("RankMapper.specialtyGPARank", map);
        int count = sqlSession.selectOne("RankMapper.specialtyGPACount", map);
        Map<String, Integer> result = new HashMap<>();
        result.put("rank", num + 1);
        result.put("count", count);
        return result;
    }
}
