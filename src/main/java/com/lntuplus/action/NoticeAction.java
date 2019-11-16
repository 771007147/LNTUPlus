package com.lntuplus.action;

import com.lntuplus.utils.OkHttpUtils;
import okhttp3.Call;
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

public class NoticeAction {

    private static final Logger logger = LoggerFactory.getLogger(NoticeAction.class);
    private static String mNoticeUrl = "http://jwzx.lntu.edu.cn";
//    private static String mNoticeUrl = "http://202.199.224.121:8090/lntu/pub_message/messagesplitepageopenwindow.jsp?&fid=5100&curid=5100&curformat=c&cursort=c&fmodulecode=5100&modulecode=5100&messagefid=5100&pageformat=pagesplite&oderitem=&seq=&topage=";
//    private static String mItemUrl = "http://202.199.224.121:8090/lntu/";

    public List<Map> get() {
        List<Map> list = requestMainPage(mNoticeUrl);
        if (list == null) {
            return null;
        }
        return list;
    }

    private List<Map> requestMainPage(String url) {
        Map<String, String> map = new HashMap<>();
        Call call = OkHttpUtils.getInstance().getExecuteCall(url);
        String data;
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                data = response.body().string();
                List<Map> noticeList = parseNotice(data);
                if (noticeList == null) {
                    return null;
                }
                return noticeList;
            }

        } catch (IOException e1) {
            System.out.println("main page request connect error");
            return null;
        }
        return null;
    }

    private List<Map> parseNotice(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByClass("vsb-space school-news school-news1").get(0);
        Elements lis = element.getElementsByTag("li");
        List<Map> list = new ArrayList<>();
        Map<String, Object> map;
        for (int i = 0; i < lis.size(); i++) {
            map = new HashMap<>();
            map.put("title", lis.get(i).getElementsByTag("a").get(0).getElementsByTag("em").get(0).text());
            String url = lis.get(i).getElementsByTag("a").get(0).attr("href");
            String link = url.substring(0, 4).equals("info") ? mNoticeUrl + "/" + url : url;
            map.put("link", link);
            map.put("time", lis.get(i).getElementsByTag("a").get(0).getElementsByTag("span").get(0).text());
            if (link.substring(0, 11).equals("http://jwzx")) {
                Map<String, Object> page = requestPages(link);
                if (page == null) {
                    return null;
                }
                map.put("page", page);
            }
            list.add(map);
        }
        return list;
    }

    private Map<String, Object> requestPages(String link) {
        Call call = OkHttpUtils.getInstance().getExecuteCall(link);
        try {
            Response respon = call.execute();
            if (respon.isSuccessful()) {
                String data = respon.body().string();
                Map<String, Object> map = parsePages(data);
                respon.close();
                return map;
            }
        } catch (IOException e1) {
            logger.info("request pages connect error");
            return null;
        }
        return null;
    }

    private Map<String, Object> parsePages(String html) {
        Map<String, Object> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Element elements = document.getElementsByClass("tr-ri tr-ri1").get(0);
        String title = elements.getElementsByClass("tr-title").get(0).getElementsByTag("h1").get(0).text();
        String time = elements.getElementsByClass("tr-title").get(0).getElementsByTag("h3").get(0).text();
        time = time.substring(time.indexOf("时间：") + 3, time.indexOf("点击数"));
        Element main = elements.getElementsByClass("v_news_content").get(0);
        Elements ps = main.getElementsByTag("p");
        Elements ul = elements.getElementsByTag("ul");
        if (ul.size() > 0) {
            Elements li = elements.getElementsByTag("ul").get(0).getElementsByTag("li");
            List<Map<String, String>> attach = new ArrayList<>();
            for (int i = 0; i < li.size(); i++) {
                Map<String, String> item = new HashMap<>();
                Element attachment;
                try {
                    attachment = li.get(i).getElementsByTag("a").get(0);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
                String attachTitle = attachment.text();
                String attachLink = mNoticeUrl + attachment.attr("href");
                item.put("attachTitle", attachTitle);
                item.put("attachLink", attachLink);
                attach.add(item);
            }
            map.put("attach", attach);
        }

        for (int i = 0; i < ps.size(); i++) {
            list.add(ps.get(i).text());
        }
        map.put("title", title);
        map.put("time", time);
        map.put("main", list);
        return map;
    }
}
