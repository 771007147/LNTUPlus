package com.lntuplus.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lntuplus.action.SchoolNoticeAction;
import com.lntuplus.model.SchoolNoticeModel;
import com.lntuplus.utils.JDBCUtils;
import com.lntuplus.utils.TimeUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SchoolNoticeService {
    @Scheduled(cron = "0 */1 * * * ?")
    public void getSchoolNotices() {
        SchoolNoticeAction schoolNoticeAction = new SchoolNoticeAction();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();   //解决%转为\u003d
        System.out.println(TimeUtils.getTime() + ":开始查询教务公告...");
        SchoolNoticeModel schoolNoticeModel = schoolNoticeAction.get();
        Map<String, Object> noticeHashcode = JDBCUtils.query("select hashcode from schoolnotice where pNo = '0' and rNo ='0' ;");
        if (noticeHashcode == null) {
            boolean noticeSucc = JDBCUtils.update("insert into schoolnotice values ('" + schoolNoticeModel.getHashcode() + "','0','0','0','" + TimeUtils.getTime() + "');");
            if (!noticeSucc) {
                System.out.println(TimeUtils.getTime() + "保存公告HashCode失败！");
            }
        } else if ((int) noticeHashcode.get("hashcode") == schoolNoticeModel.getHashcode()) {
            System.out.println(TimeUtils.getTime() + "教务公告无更新！");
            System.out.println(TimeUtils.getTime() + ":查询教务公告完毕！");
            return;
        }

        for (int i = 0; i < 10; i++) {
            int page = i + 1;
            for (int j = 0; j < 13; j++) {
                int number = j + 1;
                List<Map> pageNotices = schoolNoticeModel.getNoitceItem(i);
                if (pageNotices.size() == 0) {
                    System.out.println("主页请求错误，Pass！pNo:" + page);
                    break;
                }
                Map<String, Map<String, String>> map = pageNotices.get(j);
                if (map.get("page").containsKey("fail")) {
                    System.out.println("公告页请求错误，Pass！pNo:" + page + ",rNo:" + number);
                    continue;
                }
                Map<String, Object> query = JDBCUtils.query(
                        "select hashcode from schoolnotice where pNo = '"
                                + page + "' and rNo ='" + number + "';");
                if (query == null) {
                    boolean success = JDBCUtils.update("insert into schoolnotice values('"
                            + pageNotices.get(j).hashCode() + "','" + page + "','" + number + "','"
                            + gson.toJson(pageNotices.get(j)) + "','" + TimeUtils.getTime() + "');");
                    if (success) {
                        System.out.println("保存教务公告成功！pNo:" + page + ",rNo:" + number);
                    } else {
                        System.out.println("保存教务公告失败！pNo:" + page + ",rNo:" + number);
                    }
                } else if ((int) query.get("hashcode") != pageNotices.get(j).hashCode()) {
                    System.out.println(JDBCUtils.update("update schoolnotice set "
                            + "hashcode = '" + pageNotices.get(j).hashCode()
                            + "',notices = '" + gson.toJson(pageNotices.get(j))
                            + "',datetime = '" + TimeUtils.getTime()
                            + "' where pNo = '" + page + "' and rNo = '" + number + "';"));
                    System.out.println("更新一条教务公告成功！pNo:" + page + ",rNo:" + number);
                }
            }
        }
        System.out.println(TimeUtils.getTime() + ":查询教务公告完毕！");
    }
}
