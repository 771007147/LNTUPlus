package com.lntuplus.action;

import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import okhttp3.Call;
import okhttp3.FormBody.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluate {

    private static final Logger logger = LoggerFactory.getLogger(Evaluate.class);
    private String mSession;
    private String evaluateUrl = "/eva/index/resultlist.jsdo?groupId=&moduleId=506";
    private String evaluateGetUrl = "/eva/index";
    private String evaluatePostUrl = "/eva/index/putresult.jsdo";
    private Call mCall;
    private Response mResponse;

    public String evaluate(String port, String session) {
        mSession = session;
        jointUrl(port);
        Map<String, Object> resp = getInfo(evaluateUrl);
        if (resp.get(Constants.STRING_SUCCESS).equals(Constants.STRING_NEW_STU)) {
            return Constants.STRING_NEW_STU;
        }
        String state = doEvaluate((String) resp.get(Constants.STRING_DATA));
        return state;
    }


    private void jointUrl(String port) {
        evaluateUrl = port + evaluateUrl;
        evaluateGetUrl = port + evaluateGetUrl;
        evaluatePostUrl = port + evaluatePostUrl;
    }

    private Map<String, Object> getInfo(String url) {
        Map<String, Object> map = new HashMap<>();
        mCall = OkHttpUtils.getInstance().getInfoCall(url, mSession);
        try {
            Response resp = mCall.execute();
            if (resp.isSuccessful()) {
                String data = resp.body().string();
                map.put(Constants.STRING_SUCCESS, Constants.STRING_SUCCESS);
                map.put(Constants.STRING_DATA, data);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            map.put(Constants.STRING_SUCCESS, Constants.STRING_NEW_STU);
        }
        map.put(Constants.STRING_SUCCESS, Constants.STRING_FAILED);
        return map;
    }

    private String doEvaluate(String Html) {
        Map<String, Object> map = new HashMap<>();
        Document document = Jsoup.parse(Html);
        Elements trs = document.getElementsByClass("infolist");
        int count = 0;
        for (int i = 0; i < trs.size(); i++) {
            String url = (trs.get(i)).attr("href").substring(1);
            url = evaluateGetUrl + url;
            String state = post(url);
            switch (state) {
                case Constants.STRING_SUCCESS:
                    System.out.println(TimeUtils.getTime() + " 评课成功！");
                    break;
                case Constants.STRING_FAILED:
                    System.out.println(TimeUtils.getTime() + " 评课post失败！");
                    count++;
                    break;
                case Constants.STRING_ERROR:
                    System.out.println(TimeUtils.getTime() + " 评课出现错误！");
                    count++;
                    break;
                default:
                    break;
            }
        }
        if (count > 0) {
            return Constants.STRING_FAILED;
        }
        return Constants.STRING_SUCCESS;
    }

    public String post(String url) {
        mCall = OkHttpUtils.getInstance().getInfoCall(url, mSession);
        try {
            mResponse = mCall.execute();
            if (mResponse.isSuccessful()) {
                String data = mResponse.body().string();
                mResponse.close();
                Document document = Jsoup.parse(data);
                Elements inputs = document.getElementsByTag("input");
                Element table = document.getElementsByTag("table").get(3);
                Elements trs = table.getElementsByTag("tr");
                List<Map<String, String>> pageData = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    Map<String, String> pageMap = new HashMap<>();
                    pageMap.put(Constants.STRING_DATA, inputs.get(i).val());
                    pageMap.put(Constants.STRING_NAME, inputs.get(i).attr("name"));
                }
                List<List<Map<String, String>>> infoData = new ArrayList<>();
                for (int i = 1; i < 13; i++) {
                    List<Map<String, String>> infoItem = new ArrayList<>();
                    for (int j = 0; j < 5; j++) {
                        Map<String, String> map = new HashMap<>();
                        map.put(Constants.STRING_DATA, trs.get(i).getElementsByTag("td").get(2)
                                .getElementsByTag("input").get(j).val());
                        map.put(Constants.STRING_NAME, trs.get(i).getElementsByTag("td").get(2)
                                .getElementsByTag("input").get(j).attr("name"));
                        infoItem.add(map);
                    }
                    infoData.add(infoItem);
                }
                Builder formBody = new Builder();
                for (int i = 0; i < 8; i++) {
                    formBody.add(pageData.get(i).get(Constants.STRING_NAME), pageData.get(i).get(Constants.STRING_DATA));
                }
                for (int i = 0; i < 12; i++) {
                    for (int j = 0; j < 5; j++) {
                        formBody.add(infoData.get(i).get(j).get(Constants.STRING_NAME), infoData.get(i).get(j).get(Constants.STRING_DATA));
                    }
                }
                RequestBody requestBody = formBody.build();
                mCall = OkHttpUtils.getInstance().getInfoCallRequestBody(evaluatePostUrl, mSession, requestBody);
                mResponse = mCall.execute();
                if (mResponse.isSuccessful()) {
                    return Constants.STRING_SUCCESS;
                } else {
                    return Constants.STRING_FAILED;
                }
            }
        } catch (IOException e) {
            return Constants.STRING_ERROR;
        }
        return Constants.STRING_ERROR;
    }

}
