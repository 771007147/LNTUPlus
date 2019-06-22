package com.lntuplus.service;

import com.lntuplus.action.HelloAction;
import com.lntuplus.model.HelloModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;
import java.util.List;

@Controller
public class HelloService implements InitializingBean {
    //全局Context
    @Autowired
    private ServletContext servletContext;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void get() {
        getHello();
    }

    //首次启动执行该代码
    @Override
    public void afterPropertiesSet() {
        getHello();
    }

    public void getHello() {
        List<HelloModel> list = new HelloAction().get();
        servletContext.setAttribute("hello", list);
        return;
    }

}
