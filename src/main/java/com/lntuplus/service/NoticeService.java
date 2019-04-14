package com.lntuplus.service;

import com.lntuplus.action.NoticeAction;
import com.lntuplus.utils.TimeUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

@Component
public class NoticeService implements InitializingBean {

    //全局Context
    @Autowired
    private ServletContext servletContext;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void get() {
        getNotice();
    }

    //首次启动执行该代码
    @Override
    public void afterPropertiesSet() {
        getNotice();
    }

    private void getNotice() {

        System.out.println(TimeUtils.getTime() + ":开始查询教务公告...");
        List<Map> data = new NoticeAction().get();
        if (data == null) {
            System.out.println(TimeUtils.getTime() + ":查询教务公告失败！");
            return;
        }
        servletContext.setAttribute("notice", data);
        System.out.println(TimeUtils.getTime() + ":查询教务公告完毕！");
        return;
    }
}
