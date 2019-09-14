package com.lntuplus.controller;

import com.google.gson.Gson;
import com.lntuplus.action.LoginAction;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.GsonUtils;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import com.sun.tools.internal.jxc.ap.Const;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
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
@RequestMapping(value = "/cet")
public class CETController {
    //全局Context
    @Autowired
    private ServletContext servletContext;

    private String mCetUrl = "/student/skilltest/skilltest.jsdo?groupId=&moduleId=2090";
    private String mEnrollUrl = "/foreignlanguage/foreignlanguage_do.jsp";
    private String mPostUrl = "/foreignlanguage/foreignlanguage_win.jsp";
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();
    private Call mCall;
    private Response mResponse;

    @ResponseBody
    @RequestMapping(value = "/get")
    public String get(HttpServletRequest req) {
        String session;
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        String port = (String) servletContext.getAttribute("port");

        Map<String, String> loginMap = new LoginAction().login(number, password,port);
        if (!loginMap.get("success").equals("success")) {
            map.put("success", loginMap.get("success"));
            System.out.println(TimeUtils.getTime() + " Login失败：" + loginMap.get("success"));
            return gson.toJson(map);
        } else {
            session = loginMap.get("session");
            port = loginMap.get("port");
        }
        String cetUrl = port + mCetUrl;
        mCall = mOkHttpUtils.getInfoCall(cetUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                List<CetModel> list = parseCet(html);
                map.put(Constants.STRING_DATA, list);
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
                return gson.toJson(map);
            }
        } catch (IOException e) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_FAILED);
            System.out.println("get cet failed");
        }
        map.put(Constants.STRING_SUCCESS, Constants.STRING_FAILED);
        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/enroll")
    public String enroll(HttpServletRequest req) {
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
        String enrollUrl = port + mEnrollUrl;
        mCall = mOkHttpUtils.getInfoCall(enrollUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                EnrollModel enrollModel = parseEnroll(html);
                map.put(Constants.STRING_DATA, enrollModel);
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
                return gson.toJson(map);
            }
        } catch (IOException e) {
            System.out.println("get cet failed");
        }
        map.put(Constants.STRING_SUCCESS, Constants.STRING_FAILED);
        return gson.toJson(map);
    }

    @ResponseBody
    @RequestMapping(value = "/post")
    public String post(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();
        Gson gson = GsonUtils.getInstance();
        String number = req.getParameter("number");
        String password = req.getParameter("password");
        String notifyid = req.getParameter("notifyid");
        String id = req.getParameter("id");
        String rz = req.getParameter("rz");
        String port = (String) servletContext.getAttribute("port");

        Map<String, String> loginMap = mOkHttpUtils.login(number, password,port);
        if (!loginMap.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
            map.put(Constants.STRING_SUCCESS, loginMap.get(Constants.STRING_SUCCESS));
            return gson.toJson(map);
        }
//        String port = loginMap.get(Constants.STRING_PORT);
        String session = loginMap.get(Constants.STRING_SESSION);
        String postUrl = port + mPostUrl;
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("notifyid", notifyid);
        builder.add("id", id);
        builder.add("rz", rz);
        RequestBody requestBody = builder.build();
        mCall = mOkHttpUtils.getInfoCallRequestBody(postUrl, session, requestBody);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
            } else {
                map.put(Constants.STRING_SUCCESS, Constants.STRING_FAILED);
            }
        } catch (IOException e) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
        }
        return gson.toJson(map);
    }

    private List<CetModel> parseCet(String html) {
        Document document = Jsoup.parse(html);
        Element table = document.getElementsByClass("infolist_tab").get(1);
        Elements trs = table.getElementsByTag("tr");
        if (trs.size() == 2) {
            return new ArrayList<>();
        }
        List<CetModel> list = new ArrayList<>();
        for (int i = 1; i < trs.size() - 1; i++) {
            Elements tds = trs.get(i).getElementsByTag("td");
            CetModel cetModel = new CetModel();
            cetModel.setName(tds.get(0).text());
            cetModel.setDate(tds.get(1).text());
            cetModel.setNumber(tds.get(2).text());
            cetModel.setCard(tds.get(3).text());
            cetModel.setScore(tds.get(4).text());
            cetModel.setCancel(tds.get(5).text());
            list.add(cetModel);
        }
        return list;
    }

    private EnrollModel parseEnroll(String html) {
        Document document = Jsoup.parse(html);
        Element table = document.getElementsByClass("t13").get(1);
        Elements tds = table.getElementsByTag("td");
        EnrollModel enrollModel = new EnrollModel();
        enrollModel.setNumber(tds.get(1).text());
        enrollModel.setImgUrl(tds.get(2).getElementsByTag("img").get(0).attr("src"));
        enrollModel.setName(tds.get(4).text());
        enrollModel.setSex(tds.get(6).text());
        enrollModel.setiClass(tds.get(8).text());
        enrollModel.setProfession(tds.get(10).text());
        enrollModel.setFaculty(tds.get(12).text());
        enrollModel.setCome(tds.get(14).text());
        enrollModel.setGrade(tds.get(16).text());
        enrollModel.setLength(tds.get(18).text());
        enrollModel.setEducation(tds.get(20).text());
        enrollModel.setCard(tds.get(22).text());
        enrollModel.setYear(tds.get(24).text());
        enrollModel.setTerm(tds.get(26).text());
        enrollModel.setTime(tds.get(28).text());
        enrollModel.setRegistered(tds.get(30).text());
        enrollModel.setChoose(tds.get(32).getElementsByTag("input").get(1).text());
        enrollModel.setNotifiyid(tds.get(32).getElementsByTag("input").get(1).attr("value"));
        enrollModel.setId(table.getElementsByTag("input").get(2).attr("value"));
        enrollModel.setRz(table.getElementsByTag("input").get(3).attr("value"));
        return enrollModel;
    }
}

class CetModel {
    private String name;
    private String date;
    private String number;
    private String card;
    private String score;
    private String cancel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }
}

class EnrollModel {
    private String number;
    private String imgUrl;
    private String name;
    private String sex;
    private String iClass;
    private String profession;
    private String faculty;
    private String come;
    private String grade;
    private String length;
    private String education;
    private String card;
    private String year;
    private String term;
    private String time;
    private String registered;
    private String choose;
    private String notifiyid;
    private String id;
    private String rz;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getiClass() {
        return iClass;
    }

    public void setiClass(String iClass) {
        this.iClass = iClass;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getCome() {
        return come;
    }

    public void setCome(String come) {
        this.come = come;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getNotifiyid() {
        return notifiyid;
    }

    public void setNotifiyid(String notifiyid) {
        this.notifiyid = notifiyid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRz() {
        return rz;
    }

    public void setRz(String rz) {
        this.rz = rz;
    }
}
