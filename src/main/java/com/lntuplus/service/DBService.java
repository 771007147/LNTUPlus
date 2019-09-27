package com.lntuplus.service;

import com.lntuplus.action.ArticleAction;
import com.lntuplus.action.EverydayAction;
import com.lntuplus.action.HelloAction;
import com.lntuplus.model.EverydayModel;
import com.lntuplus.model.HelloModel;
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
public class DBService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DBService.class);
    @Autowired
    private ServletContext servletContext;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void get() {
        getHello();
        getEveryday();
        getArticle();
    }

    //首次启动执行该代码
    @Override
    public void afterPropertiesSet() {
        getHello();
        getEveryday();
        getArticle();
    }

    public void getHello() {
        List<HelloModel> list = new HelloAction().get();
        servletContext.setAttribute("hello", list);
        return;
    }

    public void getEveryday() {
        EverydayAction everydayAction = new EverydayAction();
        EverydayModel everydayModel = everydayAction.get();
        servletContext.setAttribute("everyday", everydayModel);
        return;
    }

    public void getArticle() {
        ArticleAction articleAction = new ArticleAction();
        Map<String, Object> map = articleAction.get();
        servletContext.setAttribute("article", map);
        return;
    }

}
