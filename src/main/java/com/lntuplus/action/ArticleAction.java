package com.lntuplus.action;

import com.lntuplus.model.ActivityModel;
import com.lntuplus.model.ArticleModel;
import com.lntuplus.utils.DBSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleAction {

    public Map<String, Object> get() {
        SqlSessionFactory sqlSessionFactory = DBSessionFactory.getInstance();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, Object> map = new HashMap<>();
        List<ArticleModel> article = sqlSession.selectList("ArticleMapper.article");
        map.put("article", article);
        List<ActivityModel> activity = sqlSession.selectList("ArticleMapper.activity");
        map.put("activity", activity);
        sqlSession.close();
        return map;
    }
}
