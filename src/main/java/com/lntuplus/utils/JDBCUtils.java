package com.lntuplus.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtils {
    // 驱动程序类
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    // 数据库连接字符串
    private final static String DB_URL = "jdbc:mysql://localhost:3306/gongdaplus?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
    // 数据库账户名
    private static final String DB_ACCT = "root";
    // 数据库密码
    private static final String DB_PWD = "771007147";

    // 静态块中加载数据库驱动
    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @return Connection据库连接
     */
    public static Connection getConn() {
        try {
            return DriverManager.getConnection(DB_URL, DB_ACCT, DB_PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行查询操作
     *
     * @param sql  查询SQL语句
     * @param args SQL语句 参数
     * @return 返回查询结果
     */
    public static Map<String, Object> query(String sql, Object... args) {
        Connection conn = getConn();// 获取数据连接
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            // 设置参数
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null)
                    pst.setObject(i + 1, args[i]);
            }
            // 执行查询语句
            rs = pst.executeQuery();
            if (rs.next()) {
                return rsToMap(rs);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, pst, rs);
        }
        return null;
    }

    /**
     * 执行查询操作
     *
     * @param sql  查询SQL语句
     * @param args SQL语句 参数
     * @return 返回查询结果
     */
    public static List<Map<String, Object>> list(String sql, Object... args) {
        Connection conn = getConn();// 获取数据连接
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            // 设置参数
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null)
                    pst.setObject(i + 1, args[i]);
            }
            // 执行查询语句
            rs = pst.executeQuery();
            List<Map<String, Object>> list = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> map = rsToMap(rs);
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, pst, rs);
        }
        return null;
    }

    /**
     * 执行增删改操作
     *
     * @param sql  增删改SQL语句
     * @param args SQL语句 参数
     * @return 返回是否执行成功
     */
    public static boolean update(String sql, Object... args) {
        PreparedStatement pst = null;
        Connection conn = null;
        try {
            conn = getConn();// 获取数据库连接
            pst = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null)
                    pst.setObject(i + 1, args[i]);
            }
            int row = pst.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, pst, null);
        }
        return false;
    }

    /**
     * 关闭连接释放资源
     *
     * @param conn 数据库连接
     * @param st   Statement对象
     * @param rs   ResultSet 结果集
     */
    public static void close(Connection conn, Statement st, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (st != null)
                st.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将结果集转换成Map
     *
     * @param rs 结果集
     * @return map
     * @throws SQLException
     */
    public static Map<String, Object> rsToMap(ResultSet rs) throws SQLException {
        Map<String, Object> map = new HashMap<>();
        // 获取结果集的元信息(列名，列类型，大小，列数量等等)
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();// 获取结果集中的列的数量
        for (int i = 1; i <= count; i++) {
            // 根据列的下标获取列名称
            String columnName = rsmd.getColumnName(i);
            Object value = rs.getObject(i);
            map.put(columnName.toLowerCase(), value);
        }
        // map.get("uname");
        return map;
    }

}
