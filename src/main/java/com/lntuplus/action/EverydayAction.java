package com.lntuplus.action;

import com.lntuplus.model.EverydayModel;
import com.lntuplus.utils.DBSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class EverydayAction {

    public EverydayModel get() {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EverydayModel everydayModel = sqlSession.selectOne("EverydayMapper.select");
        sqlSession.close();
        return everydayModel;
    }
}
