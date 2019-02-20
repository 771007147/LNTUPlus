package com.lntuplus.action;

import com.google.gson.Gson;
import com.lntuplus.model.ArticleModel;
import com.lntuplus.utils.JDBCUtils;

import java.util.List;
import java.util.Map;

public class ArticleAction {

    public ArticleModel get() {
        ArticleModel articleModel = new ArticleModel();
        List<Map<String, Object>> activityMainList = JDBCUtils.list("select * from activitys order by updateTime desc");
        if (activityMainList.size() > 0) {
            articleModel.setData(activityMainList);
            articleModel.setSuccess("success");
            System.out.println("文章更新成功");
        } else {
            articleModel.setSuccess("none");
            System.out.println("暂无文章！");
        }

        return articleModel;
    }
}
