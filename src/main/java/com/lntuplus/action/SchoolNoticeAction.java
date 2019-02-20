package com.lntuplus.action;

import com.lntuplus.model.SchoolNoticeModel;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchoolNoticeAction {
    private static String mOptionUrl = "http://202.199.224.121:8090";
    private static String mNoticeUrl = "http://202.199.224.121:8090/lntu/pub_message/messagesplitepageopenwindow.jsp?&fid=5100&curid=5100&curformat=c&cursort=c&fmodulecode=5100&modulecode=5100&messagefid=5100&pageformat=pagesplite&oderitem=&seq=&topage=";
    private static String mItemUrl = "http://202.199.224.121:8090/lntu/";
    private static OkHttpClient mClient;

    public SchoolNoticeModel get() {
        SchoolNoticeModel schoolNoticeModel = new SchoolNoticeModel();
        for (int i = 1; i <= 10; i++) {
            schoolNoticeModel.addNoticeMain(requestMainPage(schoolNoticeModel, mNoticeUrl + i));
        }
        schoolNoticeModel.setHashcode();
        return schoolNoticeModel;
    }

    private List<Map> requestMainPage(SchoolNoticeModel schoolNoticeModel, String url) {
        if (mClient == null) {
            mClient = new OkHttpClient();
        }
        Request request = new Request.Builder().url(url).build();
        Call call = mClient.newCall(request);
        String data;
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                data = response.body().string();
                List<Map> noticeList = parseNotice(data);
                schoolNoticeModel.setSuccess("success");
                response.close();
                return noticeList;
            } else {
                schoolNoticeModel.setSuccess("failed");
                System.out.println("main page connect failed");
                response.close();
                return new ArrayList<>();
            }

        } catch (IOException e1) {
            schoolNoticeModel.setSuccess("error");
            System.out.println("main page request connect error");
            return new ArrayList<>();
        }

    }

    private List<Map> parseNotice(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByClass("treefilename").get(0);
        Elements tds = element.getElementsByTag("td");
        int j = 0;
        List<Map> list = new ArrayList<>();
        Map<String, Object> map;
        for (int i = 0; i < 13; i++) {
            map = new HashMap<>();
            map.put("title", (tds.get(j)).getElementsByTag("a").text());
            String link = (tds.get(j)).getElementsByTag("a").attr("href");
            link = mItemUrl + link.substring(link.indexOf("/") + 1);
            map.put("link", link);
            Map<String, List> page = requestPages(link);
            map.put("page", page);
            j++;
            map.put("time", (tds.get(j)).text());
            j += 2;
            list.add(map);
        }
        return list;
    }

    private Map<String, List> requestPages(String link) {
        Request request = new Request.Builder()
                .url(link)
                .build();

        Call call = mClient.newCall(request);
        String data;
        try {
            Response respon = call.execute();
            if (respon.isSuccessful()) {
                data = respon.body().string();
                Map<String, List> map = parsePages(data);
                respon.close();
                return map;
            } else {
                System.out.println("request pages connect failed");
                respon.close();
                Map<String, List> map = new HashMap<>();
                map.put("fail", new ArrayList());
                return map;
            }

        } catch (IOException e1) {
            System.out.println("request pages connect error");
            Map<String, List> map = new HashMap<>();
            map.put("fail", new ArrayList());
            return map;
        }
    }

    private Map<String, List> parsePages(String html) {
        Map<String, List> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("thirdcontent");
        int tableNum = elements.size();

        Element main = elements.get(0);
        Elements ps = main.getElementsByTag("p");
        for (int i = 0; i < ps.size(); i++) {
            String text = (ps.get(i)).text();
            list.add(text);
        }
        map.put("main", list);
        List<Map> listMap = new ArrayList<>();
        if (tableNum == 2) {
            Element option = elements.get(1);
            Elements as = option.getElementsByTag("a");
            for (int i = 0; i < as.size(); i++) {
                Map<String, String> mapS = new HashMap<>();
                mapS.put("link", mOptionUrl + (as.get(i)).attr("href"));
                mapS.put("text", (as.get(i)).text());
                listMap.add(mapS);
            }
            map.put("option", listMap);
        }
        return map;
    }
}
