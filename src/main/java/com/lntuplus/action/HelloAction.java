package com.lntuplus.action;

import com.lntuplus.model.HelloModel;
import com.lntuplus.utils.DBSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class HelloAction {

    public List<HelloModel> get() {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        // 获取sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<HelloModel> helloModels;
        try {
            helloModels = sqlSession.selectList("HelloMapper.select");
        } finally {
            sqlSession.close();
        }
        return helloModels;
    }
}
