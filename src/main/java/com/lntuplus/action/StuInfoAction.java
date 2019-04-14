package com.lntuplus.action;

import com.lntuplus.model.StuInfoModel;
import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StuInfoAction {

    private String mStuInfoUrl = "/student/studentinfo/studentinfo.jsdo?groupId=&moduleId=2060";
    private String mNewStuUrl = "/eva/index/resultlist.jsdo?groupId=&moduleId=506";
    private OkHttpUtils mOkHttpUtils;
    private Call mCall;
    private Response mResponse;
    private Map<String, Object> map = new HashMap<>();

    public Map<String, Object> get(String port, String session) {
        mStuInfoUrl = port + mStuInfoUrl;
        mNewStuUrl = port + mNewStuUrl;
        mOkHttpUtils = OkHttpUtils.getInstance();
        mCall = mOkHttpUtils.getInfoCall(mStuInfoUrl, session);
        try {
            mResponse = mCall.execute();
            int code = mResponse.code();
            if (code == 500) {
                mResponse.close();
                map.put(Constants.STRING_SUCCESS, Constants.STRING_NEW_STU);
                return map;
            }
            if (mResponse.isSuccessful()) {
                String html = mResponse.body().string();
                mResponse.close();
                StuInfoModel stuInfoModel = parseStuInfo(html);
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
                map.put(Constants.STRING_DATA, stuInfoModel);
                return map;
            }
        } catch (IOException e1) {
            map.put(Constants.STRING_SUCCESS, Constants.STRING_ERROR);
            return map;
        }
        mResponse.close();
        return map;
    }

    private StuInfoModel parseStuInfo(String html) {
        StuInfoModel stuInfoModel = new StuInfoModel();
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByClass("infolist_vt").get(0);
        stuInfoModel.setImgUrl((document.getElementsByTag("img").get(0)).attr("src"));
        Elements tds = element.getElementsByTag("td");
        stuInfoModel.setNumber(Integer.valueOf(tds.get(0).text()));
        stuInfoModel.setNationality((tds.get(1)).text());
        stuInfoModel.setName((tds.get(3)).text());
        stuInfoModel.setPlace((tds.get(4)).text());
        stuInfoModel.setEngName((tds.get(5)).text());
        stuInfoModel.setBirth((tds.get(6)).text());
        stuInfoModel.setTypeOfID((tds.get(7)).text());
        stuInfoModel.setPoliticalStatus((tds.get(8)).text());
        stuInfoModel.setCard((tds.get(9)).text());
        stuInfoModel.setVehicleInterval((tds.get(10)).text());
        stuInfoModel.setSex((tds.get(11)).text());
        stuInfoModel.setNation((tds.get(12)).text());
        stuInfoModel.setAcademic((tds.get(13)).text());
        stuInfoModel.setProfessional((tds.get(14)).text());
        stuInfoModel.setiClass((tds.get(15)).text());
        stuInfoModel.setStuType((tds.get(16)).text());
        stuInfoModel.setExamArea((tds.get(17)).text());
        stuInfoModel.setScore((tds.get(18)).text());
        stuInfoModel.setRegistrationNumber((tds.get(19)).text());
        stuInfoModel.setSchool((tds.get(20)).text());
        stuInfoModel.setLanguageType((tds.get(21)).text());
        stuInfoModel.setComeDate((tds.get(23)).text());
        stuInfoModel.setEnrollmentType((tds.get(24)).text());
        stuInfoModel.setGoDate((tds.get(25)).text());
        stuInfoModel.setTrainingMode((tds.get(26)).text());
        stuInfoModel.setAddress((tds.get(27)).text());
        stuInfoModel.setPhone((tds.get(29)).text());
        stuInfoModel.setEmail((tds.get(30)).text());
        stuInfoModel.setOrigin((tds.get(32)).text());
        return stuInfoModel;
    }
}
