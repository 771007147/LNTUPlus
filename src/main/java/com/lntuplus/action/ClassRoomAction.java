package com.lntuplus.action;

import com.lntuplus.model.ClassRoomModel;
import com.lntuplus.utils.TimeUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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


public class ClassRoomAction {

    private static final Logger logger = LoggerFactory.getLogger(ClassRoomAction.class);
    private static String url = "http://202.199.224.121:11180/newacademic/manager/teachresource/schedule/export_room_schedule_detail.jsp";
    private static String[] ssBuildingNameHLD = {"eyl", "yhl", "jyl", "hldjf", "hldwlsys"};
    private static String[] ssBuildingNameFX = {"bwl", "byl", "xhl", "zhl", "zyl", "zxl", "wlsys", "zljf"};
    private static String[] sBuildingIDHLD = {"6", "7", "14", "9", "11"};
    private static String[] sBuildingIDFX = {"1913", "5", "4", "12", "16", "15", "10", "8"};
    private static String sBuildingName = "null";

    public ClassRoomModel get(String weeks, String name, String campus) {
        ClassRoomModel classRoomModel = new ClassRoomModel();
        String id = nameToID(name, campus);
        if (id.equals("-1")) {
            classRoomModel.setSuccess("nameError");
            System.out.println(TimeUtils.getTime() + " 校区名错误！");
            return classRoomModel;
        }
        String newUrl = url + "?weeks=" + weeks + "&buildingid1=" + id + "&sBuildingName=" + sBuildingName;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(newUrl).build();
        Call call = client.newCall(request);
        try {
            Response roomResp = call.execute();
            if (roomResp.isSuccessful()) {
                Response data = roomResp;
                classRoomModel.setSuccess("success");
                classRoomModel.setData(parseClassRoom(data.body().string()));
                logger.info(" 查询空教室成功!name:" + name + ",weeks:" + weeks);
                roomResp.close();
            } else {
                classRoomModel.setSuccess("failed");
                logger.error("获取空教室失败！");
            }
            roomResp.close();
        } catch (IOException e1) {
            classRoomModel.setSuccess("error");
            logger.error("连接超时，获取空教室失败！");
        }
        return classRoomModel;
    }

    private List parseClassRoom(String html) {
        Document document = Jsoup.parse(html);
        Element table = document.getElementsByTag("table").get(2);
        Element tbody = table.getElementsByTag("tbody").get(0);
        Elements trs = tbody.getElementsByTag("tr");
        List<Map> classData = new ArrayList<>();
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).getElementsByTag("td");
            Map<String, Object> items = new HashMap<>();
            items.put("address", tds.get(0).text());
            items.put("num", tds.get(1).text());
            items.put("type", tds.get(2).text());
            List<Map> it = new ArrayList<>();
            it.add(findSpace(tds.get(3).toString()));
            it.add(findSpace(tds.get(4).toString()));
            it.add(findSpace(tds.get(5).toString()));
            it.add(findSpace(tds.get(6).toString()));
            it.add(findSpace(tds.get(7).toString()));
            it.add(findSpace(tds.get(8).toString()));
            it.add(findSpace(tds.get(9).toString()));
            items.put("data", it);
            classData.add(items);
        }
        return classData;
    }

    private Map findSpace(String s) {
        char[] a = s.toCharArray();
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 0);
        map.put("b", 0);
        map.put("c", 0);
        map.put("d", 0);
        map.put("e", 0);
        for (int i = 0; i < a.length; i++) {
            if (a[i] == '1') {
                map.put("a", 1);
            }
            if (a[i] == '2') {
                map.put("b", 1);
            }
            if (a[i] == '3') {
                map.put("c", 1);
            }
            if (a[i] == '4') {
                map.put("d", 1);
            }
            if (a[i] == '5') {
                map.put("e", 1);
            }
        }
        return map;
    }

    private String nameToID(String sBuildingName, String campus) {
        String[] names;
        String[] ids;
        if (campus.equals("0")) {
            names = ssBuildingNameHLD;
            ids = sBuildingIDHLD;
        } else {
            names = ssBuildingNameFX;
            ids = sBuildingIDFX;
        }
        for (int i = 0; i < names.length; i++) {
            if (sBuildingName.equals(names[i])) {
                return ids[i];
            }
        }
        return "-1";
    }
}
