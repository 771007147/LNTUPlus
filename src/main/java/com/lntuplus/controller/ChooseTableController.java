package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.LoginAction;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.GsonUtils;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import okhttp3.Call;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/choose")
public class ChooseTableController {

    //全局Context
    @Autowired
    private ServletContext servletContext;

    private String mShowPostUrl = "/student/tiyu_xk/xx_xk.jsp?groupId=&moduleId=1510";
    private String mShowUrl = "/student/tiyu_xk/xx_xk1.jsp";
    private String mPostUrl = "/student/tiyu_xk/";
    private String mChooseCheckPostUrl = "/student/tiyu_xk/xx_xk.jsp?menchi=";
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();
    private Call mCall;
    private Response mResponse;

    @ResponseBody
    @RequestMapping({"/get"})
    public String get(HttpServletRequest req) {
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        String port = (String) servletContext.getAttribute("port");

        Map<String, String> loginMap = mOkHttpUtils.login(number, password,port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
//        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        String showPostUrl = port + mShowPostUrl;
        mCall = mOkHttpUtils.getInfoCall(showPostUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String shwoUrl = port + mShowUrl;
                mResponse.close();
                mCall = mOkHttpUtils.getInfoCall(shwoUrl, session);
                System.out.println("test success");
                try {
                    mResponse = mCall.execute();
                    if (mResponse.isSuccessful()) {
                        System.out.println("choose success");
                        String html = mResponse.body().string();
                        Map<String, Object> maps = parseChoose(html);
                        return gson.toJson(maps);
                    }
                } catch (IOException e) {

                }
            }
        } catch (IOException e) {

        }
        return "??";

    }

    @ResponseBody
    @RequestMapping(value = "/choose")
    public String choose(HttpServletRequest req) {
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        String select = req.getParameter("select");
        String cNumber = req.getParameter("cNumber");
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        String port = (String) servletContext.getAttribute("port");

        Map<String, String> loginMap = mOkHttpUtils.login(number, password,port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
//        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        String choosePostUrl = port + mPostUrl + select;
        mCall = mOkHttpUtils.getInfoCall(choosePostUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                System.out.println("choose post success");
                mResponse.close();
                mCall.cancel();

            }
        } catch (IOException e) {
            System.out.println("post error");
        }
        String showUrl = port + mShowUrl;
        mCall = mOkHttpUtils.getInfoCall(showUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                boolean check = checkSuccess(cNumber, html);
                System.out.println("add success");
                return check + "";
            }
        } catch (IOException e) {
            System.out.println("add error");
        }
        return "error";
    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest req) {
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        String delete = req.getParameter("delete");
        String cNumber = req.getParameter("cNumber");
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        String port = (String) servletContext.getAttribute("port");

        Map<String, String> loginMap = mOkHttpUtils.login(number, password,port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
//        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        String postUrl = port + mPostUrl + delete;
        mCall = mOkHttpUtils.getInfoCall(postUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                System.out.println("delete post success");
                mResponse.close();
                mCall.cancel();

            }
        } catch (IOException e) {
            System.out.println("post error");
        }

        String showUrl = port + mShowUrl;
        mCall = mOkHttpUtils.getInfoCall(showUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                boolean check = checkSuccess(cNumber, html);
                System.out.println("delete success");
                return check + "";
            }
        } catch (IOException e) {
            System.out.println("delete error");
        }
        return "error";
    }

    private boolean checkSuccess(String cNumber, String html) {
        Document document = Jsoup.parse(html);
        Element table = document.getElementsByClass("infolist_tab").get(0);
        Elements trs = table.getElementsByTag("tr");
        Elements tds = trs.get(trs.size() - 1).getElementsByTag("td");
        Element td = tds.get(1);
        String number = td.text();
        if (cNumber.equals(number)) {
            return true;
        }
        return false;
    }

    private Map<String, Object> parseChoose(String html) {
        Document document = Jsoup.parse(html);
        Elements tables = document.getElementsByClass("infolist_tab");
        Element oldTable = tables.get(0);
        Elements oldTrs = oldTable.getElementsByTag("tr");
        List<OldTableModel> oldList = new ArrayList<>();
        if (oldTrs.size() > 1) {
            for (int i = 1; i < oldTrs.size(); i++) {
                Elements tds = oldTrs.get(i).getElementsByTag("td");
                OldTableModel oldTableModel = new OldTableModel();
                oldTableModel.setYear(tds.get(0).text());
                oldTableModel.setcNumber(tds.get(1).text());
                oldTableModel.setcNo(tds.get(2).text());
                oldTableModel.setCourse(tds.get(3).text());
                oldTableModel.setTeacher(tds.get(4).text());
                oldTableModel.setCredit(tds.get(5).text());
                oldTableModel.setType(tds.get(6).text());
                if (!tds.get(7).text().equals("")) {
                    oldTableModel.setDelete(tds.get(7).getElementsByTag("a").get(0).attr("href"));
                }
                oldList.add(oldTableModel);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("old", oldList);
        Element newTable = tables.get(1);
        Elements newTrs = newTable.getElementsByTag("tr");
        if (newTrs.size() == 1) {
            return new HashMap<>();
        }
        List<NewTableModel> newList = new ArrayList<>();
        for (int i = 1; i < newTrs.size(); i++) {
            Elements tds = newTrs.get(i).getElementsByTag("td");
            NewTableModel newTableModel = new NewTableModel();
            newTableModel.setYear(tds.get(0).text());
            newTableModel.setcNumber(tds.get(1).text());
            newTableModel.setcNo(tds.get(2).text());
            newTableModel.setCourse(tds.get(3).text());
            newTableModel.setTeacher(tds.get(4).text());
            newTableModel.setType(tds.get(5).text());
            newTableModel.setTime(tds.get(6).text());
            newTableModel.setAddress(tds.get(7).text());
            newTableModel.setHours(tds.get(8).text());
            newTableModel.setCredit(tds.get(9).text());
            newTableModel.setAllNum(tds.get(10).text());
            newTableModel.setSelNum(tds.get(11).text());
            newTableModel.setSex(tds.get(12).text());
            if (!tds.get(13).text().equals("")) {
                newTableModel.setSelect(tds.get(13).getElementsByTag("a").get(0).attr("href"));
            }
            newList.add(newTableModel);
        }
        map.put("new", newList);

        return map;
    }
}

class OldTableModel {
    private String year;
    private String cNumber;
    private String cNo;
    private String course;
    private String teacher;
    private String credit;
    private String type;
    private String delete;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getcNumber() {
        return cNumber;
    }

    public void setcNumber(String cNumber) {
        this.cNumber = cNumber;
    }

    public String getcNo() {
        return cNo;
    }

    public void setcNo(String cNo) {
        this.cNo = cNo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }


}

class NewTableModel {
    private String year;
    private String cNumber;
    private String cNo;
    private String course;
    private String teacher;
    private String type;
    private String time;
    private String address;
    private String hours;
    private String credit;
    private String allNum;
    private String selNum;
    private String sex;
    private String select;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getcNumber() {
        return cNumber;
    }

    public void setcNumber(String cNumber) {
        this.cNumber = cNumber;
    }

    public String getcNo() {
        return cNo;
    }

    public void setcNo(String cNo) {
        this.cNo = cNo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getAllNum() {
        return allNum;
    }

    public void setAllNum(String allNum) {
        this.allNum = allNum;
    }

    public String getSelNum() {
        return selNum;
    }

    public void setSelNum(String selNum) {
        this.selNum = selNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }
}
