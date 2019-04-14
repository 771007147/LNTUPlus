package com.lntuplus.action;

import com.lntuplus.model.ExamModel;
import com.lntuplus.model.GPAModel;
import com.lntuplus.model.ScoreModel;
import com.lntuplus.model.StuInfoModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.DBSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.*;

@Service
@EnableAsync
public class SaveAction {

    @Async
    public void saveStuInfo(StuInfoModel stuInfoData) {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        int flag = sqlSession.insert("SaveMapper.stuinfoInsert", stuInfoData);
        sqlSession.commit();
        if (flag > 0) {
            System.out.println("保存学生信息成功！学号：" + stuInfoData.getNumber());
        } else {
            System.out.println("学生信息已存在！学号：" + stuInfoData.getNumber());
        }
        sqlSession.close();
    }

    @Async
    public void saveScore(List<Map<String, Object>> scoreData) {
        if (scoreData.size() == 0) {
            System.out.println("没有成绩信息，无需保存！");
            return;
        }
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String number = null;
        List<ScoreModel> data = new ArrayList<>();
        for (int i = 0; i < scoreData.size(); i++) {
            List<ScoreModel> list = (List<ScoreModel>) scoreData.get(i).get(Constants.STRING_DATA);
            for (int j = 0; j < list.size(); j++) {
                data.add(list.get(j));
                if (number == null) {
                    number = list.get(j).getNumber();
                }
            }
        }
        int count = data.size();
        ScoreModel scoreModel = new ScoreModel();
        scoreModel.setNumber(number);
        int countSaved = sqlSession.selectOne("SaveMapper.scoreCount", scoreModel);
        if (count == countSaved) {
            System.out.println("成绩无需更新！");
            return;
        }
        int flag = sqlSession.insert("SaveMapper.scoreForeach", data);
        sqlSession.commit();
        if (flag != count - countSaved) {
            System.out.println("保存成绩失败！");
        } else {
            System.out.println("更新" + flag + "条成绩成功！");
        }
        sqlSession.close();
    }

    @Async
    public void saveExam(List<ExamModel> examData) {
        if (examData.size() == 0) {
            System.out.println("没有考试信息，无需保存！");
            return;
        }
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        int flag = sqlSession.insert("SaveMapper.scoreForeach", examData);
        sqlSession.commit();
        if (flag == 0) {
            System.out.println("考试信息无需更新！");
        } else {
            System.out.println("更新" + flag + "条考试成功！");
        }
        sqlSession.close();
    }

    @Async
    public void saveGPA(String number, double gpa) {
        if (gpa == 0.0000) {
            System.out.println("没有绩点信息，无需保存！");
            return;
        }
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        GPAModel gpaModel = new GPAModel();
        gpaModel.setNumber(number);
        gpaModel.setGpa(gpa);
        gpaModel.setDate(new Date());
        int flag = sqlSession.insert("SaveMapper.gpaInsert", gpaModel);
        sqlSession.commit();
        if (flag == 0) {
            System.out.println("GPA无需更新！");
        } else {
            System.out.println("更新GPA成功！GPA：" + gpa);
        }
        sqlSession.close();
    }


}
