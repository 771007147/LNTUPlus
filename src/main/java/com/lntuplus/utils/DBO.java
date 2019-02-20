package com.lntuplus.utils;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.activation.DataSource;

public class DBO {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/gongdaplus?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "771007147";
    private Connection conn;
    private Statement stmt;
    private DataSource ds;

    public void open() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gongdaplus?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC", "root", "771007147");
            this.stmt = this.conn.createStatement();
        } catch (Exception ex) {
            System.err.println("打开数据库时出错: " + ex.getMessage());
        }
    }

    public void close() {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            System.err.println("返还连接池出错: " + ex.getMessage());
        }
    }

    public ResultSet executeQuery(String sql)
            throws SQLException {
        ResultSet rs = null;

        rs = this.stmt.executeQuery(sql);

        return rs;
    }

    public int executeUpdate(String sql)
            throws SQLException {
        int ret = 0;

        ret = this.stmt.executeUpdate(sql);

        return ret;
    }

    public void addBatch(String sql)
            throws SQLException {
        this.stmt.addBatch(sql);
    }

    public int[] executeBatch()
            throws SQLException {
        boolean isAuto = this.conn.getAutoCommit();

        this.conn.setAutoCommit(false);
        int[] updateCounts = this.stmt.executeBatch();

        return updateCounts;
    }

    public boolean getAutoCommit()
            throws SQLException {
        return this.conn.getAutoCommit();
    }

    public void setAutoCommit(boolean auto)
            throws SQLException {
        this.conn.setAutoCommit(auto);
    }

    public void commit()
            throws SQLException {
        this.conn.commit();
    }

    public void rollBack()
            throws SQLException {
        this.conn.rollback();
    }
}
