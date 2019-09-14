package com.lntuplus.action;

import com.lntuplus.model.ExamModel;
import com.lntuplus.model.TableModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import okhttp3.Call;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class TableAction {
    ///student/currcourse/currcourse.jsdo?groupId=&moduleId=2000
    private String mYearTermUrl = "/student/currcourse/currcourse.jsdo?groupId=&moduleId=2000";
    ///student/currcourse/currcourse.jsdo?year=38&term=2
    private String mTestUrl = "/student/currcourse/currcourse.jsdo?year=";
    ///student/currcourse/stuschedule.jsdo?stuId=2779287&year=38&term=2
    private String mTableUrl = "/student/currcourse/stuschedule.jsdo?stuId=";
    private int mYear;
    private int mTerm;
    private int newYear;
    private int newTerm;
    private String mStuID;
    private Call mCall;
    private Response mResponse;
    private Map<String, Object> map = new HashMap<>();
    private List<List<List<TableModel>>> tableList = new ArrayList<>();

    public Map<String, Object> get(String port, String session) {
        getYearTerm(port + mYearTermUrl, session);
        getNewYearTerm();
        Boolean next = getNewTable(port + mTestUrl + newYear + "&term=" + newTerm, session);
        if (next == null) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
            return map;
        }
        String tableUrl;
        if (next) {
            tableUrl = port + mTableUrl + mStuID + "&year=" + newYear + "&term=" + newTerm;
        } else {
            tableUrl = port + mTableUrl + mStuID + "&year=" + mYear + "&term=" + mTerm;
        }
//        System.out.println(tableUrl);
        mCall = OkHttpUtils.getInstance().getInfoCall(tableUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                tableList = parseTable(html);
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
                map.put(Constants.STRING_DATA, tableList);
                mResponse.close();

                return map;
            }
        } catch (IOException e) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
            System.out.println(TimeUtils.getTime() + " 获取Table失败！");
            mResponse.close();
            return map;
        }
        map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
        mResponse.close();
        return map;
    }

    private List<List<List<TableModel>>> parseTable(String Html) {
        Document document = Jsoup.parse(Html);
        Elements trs = document.getElementsByClass("infolist_hr_common");
        List<List<List<TableModel>>> tableList = new ArrayList<>();
        for (int i = 0; i < trs.size(); i++) {
            Element tdRow = trs.get(i);
            Elements tds = tdRow.getElementsByTag("td");
            List<List<TableModel>> rowList = new ArrayList<>();
            for (int j = 0; j < tds.size(); j++) {
                List<TableModel> oneList = new ArrayList<>();
                String item = tds.get(j).text();
                TableModel tableModel = new TableModel();
                if (item.equals("")) {
                    rowList.add(oneList);
                    continue;
                }
                int count = countTable(item);
                String s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setCourse(s);
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTime(s);
                if (!s.contains("-")) {
                    tableModel.setStart(s);
                    tableModel.setEnd(s);
                    tableModel.setSingleDouble("0");
                } else if (s.contains("单")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("1");
                } else if (s.contains("双")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("2");
                } else {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1));
                    tableModel.setSingleDouble("0");
                }
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTeacher(s);
                if (count == 1) {
                    tableModel.setAddress(item);
                    oneList.add(tableModel);
                    rowList.add(oneList);
                    continue;
                } else {
                    s = item.substring(0, item.indexOf(" "));
                    item = item.substring(item.indexOf(" ") + 1);
                    tableModel.setAddress(s);
                    oneList.add(tableModel);
                }
                tableModel = new TableModel();
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setCourse(s);
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTime(s);
                if (!s.contains("-")) {
                    tableModel.setStart(s);
                    tableModel.setEnd(s);
                    tableModel.setSingleDouble("0");
                } else if (s.contains("单")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("1");
                } else if (s.contains("双")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("2");
                } else {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1));
                    tableModel.setSingleDouble("0");
                }

                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTeacher(s);
                if (count == 2) {
                    tableModel.setAddress(item);
                    oneList.add(tableModel);
                    rowList.add(oneList);
                    continue;
                } else {
                    s = item.substring(0, item.indexOf(" "));
                    item = item.substring(item.indexOf(" ") + 1);
                    tableModel.setAddress(s);
                    oneList.add(tableModel);
                }
                tableModel = new TableModel();
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setCourse(s);
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTime(s);
                if (!s.contains("-")) {
                    tableModel.setStart(s);
                    tableModel.setEnd(s);
                    tableModel.setSingleDouble("0");
                } else if (s.contains("单")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("1");
                } else if (s.contains("双")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("2");
                } else {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1));
                    tableModel.setSingleDouble("0");
                }

                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTeacher(s);
                if (count == 3) {
                    tableModel.setAddress(item);
                    oneList.add(tableModel);
                    rowList.add(oneList);
                    continue;
                } else {
                    s = item.substring(0, item.indexOf(" "));
                    item = item.substring(item.indexOf(" ") + 1);
                    tableModel.setAddress(s);
                    oneList.add(tableModel);
                }
                tableModel = new TableModel();
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setCourse(s);
                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTime(s);
                if (!s.contains("-")) {
                    tableModel.setStart(s);
                    tableModel.setEnd(s);
                    tableModel.setSingleDouble("0");
                } else if (s.contains("单")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("1");
                } else if (s.contains("双")) {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
                    tableModel.setSingleDouble("2");
                } else {
                    tableModel.setStart(s.substring(0, s.indexOf("-")));
                    tableModel.setEnd(s.substring(s.indexOf("-") + 1));
                    tableModel.setSingleDouble("0");
                }

                s = item.substring(0, item.indexOf(" "));
                item = item.substring(item.indexOf(" ") + 1);
                tableModel.setTeacher(s);
                if (count == 4) {
                    tableModel.setAddress(item);
                    oneList.add(tableModel);
                    rowList.add(oneList);
                } else {
                    s = item.substring(0, item.indexOf(" "));
                    tableModel.setAddress(s);
                    oneList.add(tableModel);
                }
            }
            tableList.add(rowList);
            i++;
        }

        return tableList;
    }

    private void getYearTerm(String url, String session) {
        mCall = OkHttpUtils.getInstance().getInfoCall(url, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                parseConstant(html);
            }else{
                System.out.println("getYearTerm failed");
            }
        } catch (IOException e) {
            System.out.println("getYearTerm error");
        }
    }

    private boolean parseConstant(String html) {
        Document document = Jsoup.parse(html);
        String input = document.getElementsByTag("input").get(0).attr("onClick");
        mStuID = input.substring(input.indexOf("stuId=") + 6, input.indexOf("&year"));
        System.out.println(mStuID);
        mYear = Integer.valueOf(input.substring(input.indexOf("year=") + 5, input.indexOf("&term")));
        System.out.println(mYear);
        mTerm = Integer.valueOf(input.substring(input.indexOf("term=") + 5, input.indexOf("',''")));
//        System.out.println("stuid" + mStuID + ",myear" + mYear + ",mterm" + mTerm);
        if (mStuID != null && mYear != 0 && mTerm != 0) {
            return true;
        }
        return false;
    }

    private void getNewYearTerm() {
        if (mTerm == 2) {
            newYear = mYear + 1;
            newTerm = 1;
        } else {
            newYear = mYear;
            newTerm = 2;
        }
    }

    private Boolean getNewTable(String url, String session) {
        mCall = OkHttpUtils.getInstance().getInfoCall(url, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                Boolean next = checkNewTable(html);
                mResponse.close();

                return next;
            }
        } catch (IOException e) {
            System.out.println(TimeUtils.getTime() + " getNewTable失败！");
            return null;
        }
        mResponse.close();
        return null;
    }

    private Boolean checkNewTable(String html) {
        Document document = Jsoup.parse(html);
        Element table = document.getElementsByClass("infolist_tab").get(0);
        Elements trs = table.getElementsByTag("tr");
        if (trs.size() > 1) {
            return true;
        }
        return null;
    }

    private int countTable(String s) {
        int n = 0;// 计数器
        int index;// 指定字符的长度
        index = s.indexOf(" ");
        while (index != -1) {
            n++;
            index = s.indexOf(" ", index + 1);
        }
        return (n + 1) / 4;
    }

    private TableModel parseItem(String s, String item) {
        TableModel tableModel = new TableModel();
        s = item.substring(0, item.indexOf(" "));
        item = item.substring(item.indexOf(" ") + 1);
        tableModel.setCourse(s);
        s = item.substring(0, item.indexOf(" "));
        item = item.substring(item.indexOf(" ") + 1);
        tableModel.setTime(s);
        if (!s.contains("-")) {
            tableModel.setStart(s);
            tableModel.setEnd(s);
            tableModel.setSingleDouble("0");
        } else if (s.contains("单")) {
            tableModel.setStart(s.substring(0, s.indexOf("-")));
            tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
            tableModel.setSingleDouble("1");
        } else if (s.contains("双")) {
            tableModel.setStart(s.substring(0, s.indexOf("-")));
            tableModel.setEnd(s.substring(s.indexOf("-") + 1, s.length() - 1));
            tableModel.setSingleDouble("2");
        } else {
            tableModel.setStart(s.substring(0, s.indexOf("-")));
            tableModel.setEnd(s.substring(s.indexOf("-") + 1));
            tableModel.setSingleDouble("0");
        }

        s = item.substring(0, item.indexOf(" "));
        item = item.substring(item.indexOf(" ") + 1);
        tableModel.setTeacher(s);
        return tableModel;
    }
}
