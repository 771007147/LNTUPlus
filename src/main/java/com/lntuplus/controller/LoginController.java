package com.lntuplus.controller;

import com.lntuplus.action.AsyncAction;
import com.lntuplus.interfaces.ILoginController;
import com.lntuplus.model.ExamModel;
import com.lntuplus.model.StuInfoModel;
import com.lntuplus.model.TableModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Controller
@RequestMapping(value = "/login")
@EnableAsync
public class LoginController implements ILoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();

    @Autowired
    private AsyncAction mAsyncAction;

    @Autowired
    private ServletContext servletContext;

    @ResponseBody
    @RequestMapping(value = "/get")
    @SuppressWarnings("unchecked")
    public Object get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setContentType("application/json; charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        logger.info(number + " 登录中...");
        String port = (String) servletContext.getAttribute("port");
        if (port.equals(Constants.STRING_ERROR)) {
            logger.info("Port error");
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
            return map;
        }
        Map<String, String> loginMap = mOkHttpUtils.login(number, password, port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        String session = loginMap.get(Constants.STRING_SESSION);

        Future<Map<String, Object>> stuInfoFuture = mAsyncAction.getStuInfo(port, session, password);
        Future<Map<String, Object>> scoreFuture = mAsyncAction.getScore(port, session, number);
        Future<Map<String, Object>> examFuture = mAsyncAction.getExam(port, session, number);
        Future<Map<String, Object>> tableFuture = mAsyncAction.getTable(port, session);

        long start = System.currentTimeMillis();
        while (true) {
            if (stuInfoFuture.isDone() && scoreFuture.isDone() && examFuture.isDone() && tableFuture.isDone()) {
                break;
            }
            Thread.sleep(500);
            if ((System.currentTimeMillis() - start) / 1000 > 10) {
                logger.error("轮询等待多线程返回超时！");
                stuInfoFuture.cancel(true);
                scoreFuture.cancel(true);
                examFuture.cancel(true);
                tableFuture.cancel(true);
                map.put(Constants.STRING_SUCCESS, Constants.STRING_TIME);
                return map;
            }
        }
        Map<String, Object> stuInfoMap = stuInfoFuture.get();
        if (!stuInfoMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            logger.debug("getStuInfo失败：" + stuInfoMap.get(Constants.STRING_SUCCESS));
            map.put(Constants.STRING_SUCCESS, stuInfoMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        StuInfoModel stuInfoData = (StuInfoModel) stuInfoMap.get(Constants.STRING_DATA);
        stuInfoData.setPassword(password);
        map.put(Constants.STRING_STU_INFO, stuInfoData);

        Map<String, Object> scoreMap = scoreFuture.get();
        if (!scoreMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            logger.debug("scoreMap：" + stuInfoMap.get(Constants.STRING_SUCCESS));
            map.put(Constants.STRING_SUCCESS, scoreMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        List<Map<String, Object>> scoreData = (List<Map<String, Object>>) scoreMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_SCORE, scoreData);
        map.put(Constants.STRING_GPA, scoreMap.get(Constants.STRING_GPA));
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);

        Map<String, Object> examMap = examFuture.get();
        if (!examMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            logger.debug("examMap：" + stuInfoMap.get(Constants.STRING_SUCCESS));
            map.put(Constants.STRING_SUCCESS, examMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        List<ExamModel> examData = (List<ExamModel>) examMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_EXAM, examData);

        Map<String, Object> tableMap = tableFuture.get();
        if (!tableMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, tableMap.get(Constants.STRING_SUCCESS));
            logger.debug("tableMap：" + tableMap.get(Constants.STRING_SUCCESS));
            return map;
        }
        List<List<List<TableModel>>> tableData = (List<List<List<TableModel>>>) tableMap.get(Constants.STRING_DATA);
        map.put(Constants.STRING_TABLE, tableData);
////        reflectStuInfo(stuInfoData);
        logger.info("登陆成功！学号：" + number);
        return map;
    }

    @Override
    public void stuInfoCallback(Map<String, Object> map) {

    }

    @Override
    public void scoreCallback(Map<String, Object> map) {

    }

    @Override
    public void examCallback(Map<String, Object> map) {

    }

    @Override
    public void tableCallback(Map<String, Object> map) {

    }
}
