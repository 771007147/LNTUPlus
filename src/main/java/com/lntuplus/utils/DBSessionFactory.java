package com.lntuplus.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class DBSessionFactory {
    // 指定全局配置文件
    private static String CONFIG = "mybatis-config.xml";
    // 读取配置文件
    private static InputStream sInputStream = null;

    private static DBSessionFactory sDBSessionFactory = null;

    private static SqlSessionFactory sSqlSessionFactory = null;

    private DBSessionFactory() {
        try {
            sInputStream = Resources.getResourceAsStream(CONFIG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sSqlSessionFactory = new SqlSessionFactoryBuilder().build(sInputStream);
    }

    public static SqlSessionFactory getInstance() {
        if (sDBSessionFactory == null) {
            synchronized (DBSessionFactory.class) {
                if (sDBSessionFactory == null) {
                    sDBSessionFactory = new DBSessionFactory();
                }
            }
        }
        return sDBSessionFactory.getSqlSessionFactory();
    }

    private SqlSessionFactory getSqlSessionFactory() {
        return sSqlSessionFactory;
    }
}
