package com.lntuplus.service;

import com.lntuplus.action.NoticeAction;
import com.lntuplus.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

@Controller
public class NoticeService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(NoticeService.class);
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
        logger.info("开始查询教务公告...");
        List<Map> data = new NoticeAction().get();
        if (data == null) {
            logger.info("查询教务公告失败!");
            return;
        }
        servletContext.setAttribute("notice", data);
        logger.info("查询教务公告完毕!");
        return;
    }
}
