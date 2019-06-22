package com.lntuplus.action;

import com.lntuplus.model.ExamModel;
import com.lntuplus.model.StuInfoModel;
import com.lntuplus.model.TableModel;
import com.lntuplus.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class AsyncAction {

    @Autowired
    private SaveAction mSaveAction;

    @Async
    public Future<Map<String, Object>> getStuInfo(String port, String session, String password) {
        System.out.println("获取学生信息..");
        Map<String, Object> stuInfoMap = new StuInfoAction().get(port, session);
        if (stuInfoMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            StuInfoModel stuInfoData = (StuInfoModel) stuInfoMap.get(Constants.STRING_DATA);
            stuInfoData.setPassword(password);
//            mSaveAction.saveStuInfo(stuInfoData);
        }
        System.out.println("获取学生信息完毕");
        return new AsyncResult<>(stuInfoMap);
    }

    @Async
    public Future<Map<String, Object>> getScore(String port, String session, String number) {
        System.out.println("获取学生成绩..");
        Map<String, Object> scoreMap = new ScoreAction().get(port, session, number);
        if (scoreMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            List<Map<String, Object>> scoreData = (List<Map<String, Object>>) scoreMap.get(Constants.STRING_DATA);
            double gpa = (double) scoreMap.get(Constants.STRING_GPA);
//            mSaveAction.saveScore(scoreData);
//            mSaveAction.saveGPA(number, gpa);
        }
        System.out.println("获取学生成绩完毕");
        return new AsyncResult<>(scoreMap);
    }

    @Async
    public Future<Map<String, Object>> getExam(String port, String session, String number) {
        System.out.println("获取学生考试..");
        Map<String, Object> examMap = new ExamAction().get(port, session, number);
        if (examMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            List<ExamModel> examData = (List<ExamModel>) examMap.get(Constants.STRING_DATA);
//            mSaveAction.saveExam(examData);
        }
        System.out.println("获取学生考试完毕");
        return new AsyncResult<>(examMap);
    }

    @Async
    public Future<Map<String, Object>> getTable(String port, String session) {
        System.out.println("获取学生课表..");
        Map<String, Object> tableMap = new TableAction().get(port, session);
        if (tableMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {

        }
        List<List<List<TableModel>>> tableData = (List<List<List<TableModel>>>) tableMap.get(Constants.STRING_DATA);
        System.out.println("获取学生课表完毕");
        return new AsyncResult<>(tableMap);
    }

    @Async
    public void saveScore(List<Map<String, Object>> scoreData, String number, double gpa) {
        System.out.println("保存学生成绩..");
        mSaveAction.saveScore(scoreData);
        mSaveAction.saveGPA(number, gpa);
        System.out.println("保存学生成绩完毕");
    }

    @Async
    public void saveExam(List<ExamModel> examData) {
        System.out.println("保存学生考试..");
        mSaveAction.saveExam(examData);
        System.out.println("保存学生考试完毕");
    }
}
