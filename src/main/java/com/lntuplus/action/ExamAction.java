package com.lntuplus.action;

import com.lntuplus.model.ExamModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import okhttp3.Call;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ExamAction {

    private static final Logger logger = LoggerFactory.getLogger(ExamAction.class);

    private String mExamUrl = "/student/exam/index.jsdo?groupId=&moduleId=2030";
    private int mNumber;
    private Call mCall;
    private Response mResponse;
    private Map<String, Object> map = new HashMap<>();

    public Map<String, Object> get(String port, String session, String number) {
        this.mNumber = Integer.valueOf(number);
        mExamUrl = port + mExamUrl;
        mCall = OkHttpUtils.getInstance().getInfoCall(mExamUrl, session);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                mResponse.close();
                List<ExamModel> list = parseExam(html);
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
                map.put(Constants.STRING_DATA, list);
                return map;
            } else {
                map.put(Constants.STRING_SUCCESS, Constants.STRING_FAILED);
            }
        } catch (IOException e1) {
            if (mResponse != null) {
                mResponse.close();
            }
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
        }
        mResponse.close();
        return map;
    }

    private List<ExamModel> parseExam(String Html) {
        Document document = Jsoup.parse(Html);
        Element element = document.getElementsByClass("infolist_tab").get(0);
        Elements trs = element.getElementsByTag("tr");
        List<ExamModel> list = new ArrayList<>();
        List<ExamModel> beforeList = new ArrayList<>();
        List<ExamModel> afterList = new ArrayList<>();
        if (trs.size() == 1) {
            return new ArrayList<>();
        }
        for (int i = 1; i < trs.size(); i++) {
            Element e = trs.get(i);
            Elements tds = e.getElementsByTag("td");
            ExamModel examModel = new ExamModel();
            examModel.setNumber(mNumber);
            examModel.setCourse(tds.get(0).text());
            examModel.setDateTime(TimeUtils.toDate(tds.get(1).text().substring(0, 16)));
            examModel.setDate(tds.get(1).text().substring(0, tds.get(1).text().indexOf(" ")));
            examModel.setTime(tds.get(1).text().substring(tds.get(1).text().indexOf(" ") + 1));
            Date date = new Date();
            if (date.after(examModel.getDateTime())) {
                beforeList.add(examModel);
            } else {
                afterList.add(examModel);
            }
            examModel.setAddress(tds.get(2).text());
        }
        Collections.sort(beforeList);
        Collections.sort(afterList);
        Collections.reverse(afterList);
        list.addAll(afterList);
        list.addAll(beforeList);
        return list;
    }
}
