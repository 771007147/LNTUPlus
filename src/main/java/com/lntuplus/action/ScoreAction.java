package com.lntuplus.action;

import com.lntuplus.model.ScoreModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import com.sun.tools.internal.jxc.ap.Const;
import okhttp3.Call;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class ScoreAction {

    private String mScoreUrl = "/student/queryscore/queryscore.jsdo?groupId=&moduleId=2021";
    private Call mCall;
    private Response mResponse;
    private double mGPA = 0.0000;
    private String mPort;
    private String mSession;
    private Map<String, Object> map = new HashMap<>();
    private List<Map<String, Object>> scoreMap = new ArrayList<>();

    public Map<String, Object> get(String port, String session, String number) {
        this.mPort = port;
        this.mSession = session;
        this.mScoreUrl = port + mScoreUrl;
        mCall = OkHttpUtils.getInstance().getInfoCall(mScoreUrl, mSession);
        try {
            mResponse = mCall.execute();
            int code = mResponse.code();
            if (code == 500) {
                mResponse.close();
                String state = new Evaluate().evaluate(mPort, mSession);
                switch (state) {
                    case Constants.STRING_NEW_STU:
                        System.out.println("该新生尚未填写必填信息:" + number);
                        map.put(Constants.STRING_SUCCESS, Constants.STRING_NEW_STU);
                        break;
                    case Constants.STRING_FAILED:
                    case Constants.STRING_ERROR:
                        System.out.println(TimeUtils.getTime() + " 评课失败!");
                        break;
                    case Constants.STRING_SUCCESS:
                        map = new ScoreAction().get(mPort, mSession, number);
                        break;
                    default:
                        break;
                }
                return map;
            }
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                if (checkGraduateQuestionnaire(html)) {
                    map.put(Constants.STRING_SUCCESS, Constants.STRING_QUESTIONNAIRE);
                    return map;
                }
                scoreMap = parseScore(html, number);
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
                map.put(Constants.STRING_DATA, scoreMap);
                map.put(Constants.STRING_GPA, mGPA);
                mResponse.close();
                return map;
            }
        } catch (IOException e) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
            System.out.println(TimeUtils.getTime() + " 获取Score失败！");
        }
        mResponse.close();
        return map;
    }

    private List<Map<String, Object>> parseScore(String html, String number) {
        List<Map<String, Object>> scoreList = new ArrayList<>();
        List<ScoreModel> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Element gpaTable = document.getElementsByClass("broken_tab").get(0);
        Element gpaTr = gpaTable.getElementsByTag("tr").get(0);
        String gpaTd = (gpaTr.getElementsByTag("td").get(2)).text();
        if (gpaTd.indexOf("，") > 0) {
            mGPA = Double.valueOf(gpaTd.substring(10, gpaTd.indexOf("，")));
//            System.out.println("GPA:" + mGPA);
        }
        Element element = document.getElementsByClass("infolist_tab").get(0);
        Elements trs = element.getElementsByTag("tr");
        if (trs.size() == 1) {
            return new ArrayList<>();
        }
        Date date = new Date();
        for (int i = 1; i < trs.size(); i++) {
            String id = trs.get(i).id().substring(0, 1);
//            boolean type = trs.get(i).getElementsByTag("td").get(1).text().contains("*")||
//                    trs.get(i).getElementsByTag("td").get(1).text().contains("＊");
            if (id.equals("b")) {
                continue;
            }
            ScoreModel scoreModel = new ScoreModel();
            Elements tds = trs.get(i).getElementsByTag("td");
            scoreModel.setNumber(number);
            scoreModel.setcNumber(tds.get(0).text());
            scoreModel.setCourse(tds.get(1).text());
            scoreModel.setcNo(Integer.parseInt(tds.get(2).text()));
            scoreModel.setScore(tds.get(3).text());
            scoreModel.setCredit(Double.parseDouble(tds.get(4).text()));
            scoreModel.setExamType(tds.get(5).text());
            scoreModel.setProperties(tds.get(6).text());
            scoreModel.setRemark(tds.get(7).text());
            scoreModel.setMode(tds.get(8).text());
            scoreModel.setYear(tds.get(9).text());
            scoreModel.setDelayed(tds.get(10).text());
            scoreModel.setDatetime(date);
            if (!tds.get(11).getElementsByTag("a").attr("href").equals("")) {
                String url = mPort + "/student/queryscore/" + tds.get(11).getElementsByTag("a").attr("href");
                mCall = OkHttpUtils.getInstance().getInfoCall(url, mSession);
                try {
                    mResponse = mCall.execute();
                    if (mResponse.isSuccessful()) {
                        String detailed = mResponse.body().string();
                        if (detailed != null) {
                            Map<String, Object> map = parseDetailed(detailed);
                            if (map.get(Constants.STRING_SUCCESS).equals(Constants.STRING_SUCCESS)) {
                                scoreModel.setOpenUnit((String) map.get("openUnit"));
                                scoreModel.setTeacher((String) map.get("teacher"));
                                String scoreInfo = (String) map.get("scoreInfo");
                                scoreModel.setScoreInfo(scoreInfo != null && scoreInfo.equals("") ? null : scoreInfo);
                                String normalScore = (String) map.get("normalScore");
                                scoreModel.setNormalScore(normalScore != null && normalScore.equals("") ? null : normalScore);
                                String midScore = (String) map.get("midScore");
                                scoreModel.setMidScore(midScore != null && midScore.equals("") ? null : midScore);
                                String endScore = (String) map.get("endScore");
                                scoreModel.setEndScore(endScore != null && endScore.equals("") ? null : endScore);
                                if (map.containsKey("examRoom") && map.containsKey("seatNum")) {
                                    scoreModel.setExamRoom((Integer) map.get("examRoom"));
                                    scoreModel.setSeatNum((Integer) map.get("seatNum"));
                                }
                            }
                        }
                        mResponse.close();
                    }
                } catch (IOException e) {

                }
                mResponse.close();
            }
            list.add(scoreModel);
        }
        Collections.sort(list);
        String term = list.get(0).getYear();
        List<ScoreModel> item = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                item.add(list.get(i));
                Map<String, Object> it = new HashMap<>();
                it.put(Constants.STRING_YEAR, term);
                it.put(Constants.STRING_DATA, item);
                scoreList.add(it);
                continue;
            }
            if (!list.get(i).getYear().equals(list.get(i + 1).getYear())) {
                item.add(list.get(i));
                Map<String, Object> it = new HashMap<>();
                it.put(Constants.STRING_YEAR, term);
                it.put(Constants.STRING_DATA, item);
                scoreList.add(it);
                item = new ArrayList<>();
                term = list.get(i + 1).getYear();
                continue;
            }
            item.add(list.get(i));
        }
        return scoreList;
    }

    private Map<String, Object> parseDetailed(String html) {
        Map<String, Object> map = new HashMap<>();

        if (html.equals("500")) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_500);
            return map;
        }
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByTag("CENTER").get(0);
        if (element.getElementsByTag("table").toString().equals("")) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_FAILED);
            return map;
        }
        Element table = element.getElementsByTag("table").get(0);
        Elements trs = table.getElementsByTag("tr");
        String s = trs.get(0).text();
        if (s.contains("教室编号") && s.contains("座位号")) {
            map.put("examRoom", s.substring(s.indexOf("编号：") + 3, s.indexOf(" 座位")));
            map.put("seatNum", s.substring(s.indexOf("座位号：") + 4, s.indexOf("）")));
        }
        Elements tds = trs.get(1).getElementsByTag("td");
        map.put("openUnit", tds.get(5).text());
        map.put("teacher", tds.get(7).text());
        if (trs.get(4).text().contains("成绩信息：")) {
            map.put("scoreInfo", trs.get(4).text().substring(
                    trs.get(4).text().indexOf("成绩信息：") + 5));
        }
        tds = trs.get(6).getElementsByTag("td");
        map.put("normalScore", tds.get(0).text());
        map.put("midScore", tds.get(1).text());
        map.put("endScore", tds.get(2).text());
        map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
        return map;
    }

    private boolean checkGraduateQuestionnaire(String html) {
        Document document = Jsoup.parse(html);
        String title = document.getElementsByTag("title").get(0).text();
        if (title.equals("问卷调查")) {
            return true;
        }
        return false;
    }

}
