package com.lawyus.study.postgres;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
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
 * @since 2020/3/29
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

    public void template() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        /*private String serverName = "localhost";
        private String databaseName = "";
        private String user;
        private String password;
        private int portNumber = 0;*/
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("postgres");
        dataSource.setCurrentSchema("public");
        dataSource.setUser("postgres");
        dataSource.setPassword("chengke123");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
