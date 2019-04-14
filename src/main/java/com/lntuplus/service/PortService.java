package com.lntuplus.service;

import com.lntuplus.utils.Constants;
import com.lntuplus.utils.DBSessionFactory;
import com.lntuplus.utils.OkHttpUtils;
import com.lntuplus.utils.TimeUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class PortService {
    private String mPort;
    private OkHttpUtils mOkHttpUtils = OkHttpUtils.getInstance();

    @Scheduled(cron = "0/10 * * * * ?")
    public void getPort() {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        mPort = mOkHttpUtils.getUseablePort();
        if (mPort.equals(Constants.STRING_FAILED) || mPort.equals(Constants.STRING_ERROR)) {
            System.out.println(TimeUtils.getTime() + " 获取Port失败，教务在线爆炸！");
        }
        int count = sqlSession.selectOne("PortMapper.selectCount");
        if (count == 0) {
            int flag = sqlSession.insert("PortMapper.insert", mPort);
            sqlSession.commit();
            if (flag == 0) {
                System.out.println(TimeUtils.getTime() + " 插入Port失败！");
            } else {
                System.out.println(TimeUtils.getTime() + " 插入Port成功！");
                System.out.println(mPort);
            }
            return;
        } else {
            String port = sqlSession.selectOne("PortMapper.select");
            if (mPort.equals(port)) {
                return;
            }
            int flag = sqlSession.update("PortMapper.update", mPort);
            sqlSession.commit();
            if (flag == 0) {
                System.out.println(TimeUtils.getTime() + " 更新Port失败！");
            } else {
                System.out.println(TimeUtils.getTime() + " 更新Port成功！");
                System.out.println(mPort);
            }
        }
        sqlSession.close();
    }
}