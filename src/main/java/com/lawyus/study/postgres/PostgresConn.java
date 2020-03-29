package com.lawyus.study.postgres;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * TODO describe please!
 *
 * @author yushing
 * @date 2020/3/29
 */
public class PostgresConn {

    // 查询schema列表
    String schemaSql = "select * from pg_namespace";

    public static void main(String[] args) {

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:54320/postgres?useSSL=false&characterEncoding=utf8", "postgres", "FlyPG123.");
            //c.setAutoCommit(false);

            /*JdbcRowSet rowSet = RowSetProvider.newFactory().createJdbcRowSet();
            rowSet.setCommand("use postgres");
            rowSet.execute();*/


            stmt = c.createStatement();
            String s = "drop database pgdb1";
            stmt.execute(s);
            stmt.close();
            //c.commit();
            c.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
