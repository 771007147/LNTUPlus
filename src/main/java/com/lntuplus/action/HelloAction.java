package com.lntuplus.action;

import com.lntuplus.model.HelloModel;
import com.lntuplus.utils.DBSessionFactory;
import com.lntuplus.utils.GsonUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class HelloAction {

    public String get() {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        // 获取sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<HelloModel> helloModels;
        try {
            helloModels = sqlSession.selectList("HelloMapper.select");
            System.out.println("启动图获取成功!");
        } finally {
            sqlSession.close();
        }
        return GsonUtils.getInstance().toJson(helloModels);
    }
}
