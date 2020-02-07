package com.lntuplus.service;

import com.lntuplus.utils.Constants;
import com.lntuplus.utils.OkHttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletContext;

@Controller
@EnableAsync
public class PortService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PortService.class);

    @Autowired
    private ServletContext servletContext;


    private String mPort;
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();

    @Override
    public void afterPropertiesSet() {
        getPort();
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void getPort() {
//        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
//        SqlSession sqlSession = sqlSessionFactory.openSession();
        mPort = mOkHttpUtils.getUseablePort();
        if (mPort.equals(Constants.STRING_ERROR)) {
            logger.error("获取Port失败，教务在线爆炸！");
        } else {
            logger.info("获取可用端口:" + mPort);
        }
        servletContext.setAttribute("port", mPort);
        return;
//        int count = sqlSession.selectOne("PortMapper.selectCount");
//        if (count == 0) {
//            int flag = sqlSession.insert("PortMapper.insert", mPort);
//            sqlSession.commit();
//            if (flag == 0) {
//                System.out.println(TimeUtils.getTime() + " 插入Port失败！");
//            } else {
//                System.out.println(TimeUtils.getTime() + " 插入Port成功！");
//                System.out.println(mPort);
//            }
//            return;
//        } else {
//            String port = sqlSession.selectOne("PortMapper.select");
//            if (mPort.equals(port)) {
//                return;
//            }
//            int flag = sqlSession.update("PortMapper.update", mPort);
//            sqlSession.commit();
//            if (flag == 0) {
//                System.out.println(TimeUtils.getTime() + " 更新Port失败！");
//            } else {
//                System.out.println(TimeUtils.getTime() + " 更新Port成功！");
//                System.out.println(mPort);
//            }
//        }
//        sqlSession.close();
    }
}