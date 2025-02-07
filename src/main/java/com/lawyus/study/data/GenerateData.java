package com.lawyus.study.data;

import com.github.javafaker.Faker;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class GenerateData {
    // JDBC连接池参数
    // rewriteBatchedStatements=true 需要加上这个参数才会批量执行SQL语句
    private static final String DB_URL = "jdbc:mysql://localhost:3306/develop?rewriteBatchedStatements=true";
    private static final String USER = "root";
    private static final String PASS = "develop";

    // 创建表的 SQL 语句
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS test_data_jdbc ("
            + "id INT AUTO_INCREMENT PRIMARY KEY,"
            + "name VARCHAR(50),"
            + "age TINYINT,"
            + "email VARCHAR(255),"
            + "address VARCHAR(255),"
            + "phone_number VARCHAR(15),"
            + "job VARCHAR(100),"
            + "company VARCHAR(100),"
            + "salary DECIMAL(10, 2),"
            + "is_employed TINYINT(1),"
            + "birth_date DATE,"
            + "registration_date DATETIME,"
            + "height FLOAT,"
            + "weight FLOAT,"
            + "website_url VARCHAR(255),"
            + "created_at TIMESTAMP)";

    // 插入数据的 SQL 语句
    private static final String INSERT_QUERY = "INSERT INTO test_data_jdbc (name, age, email, address, phone_number, job, company, salary, is_employed,"
            + " birth_date, registration_date, height, weight, website_url, created_at)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // 数据源
    private static DataSource dataSource;
    // 批次大小
    private static final int BATCH_SIZE = 1000;
    // 总记录数
    private static final int TOTAL_RECORDS = 10000000;
    // 线程池大小
    private static final int THREADS = 10;
    // 最小连接数
    private static final int MIN_CONNECTIONS = 10;
    // 最大连接数
    private static final int MAX_CONNECTIONS = 10;

    public static void main(String[] args) {

        // 创建HikariCP连接池配置
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASS);
        config.setMinimumIdle(MIN_CONNECTIONS);
        config.setMaximumPoolSize(MAX_CONNECTIONS);
        log.info("HikariConfig: {}", config);

        // 创建HikariCP数据源
        dataSource = new HikariDataSource(config);

        // 创建固定大小的线程池
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // 创建表
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_QUERY);
        } catch (SQLException e) {
            log.error("创建表错误：{}", e.getMessage());
            return;
        }

        // 提交任务到线程池
        for (int i = 0; i < TOTAL_RECORDS / BATCH_SIZE; i++) {
            int batchNumber = i;
            executor.submit(() -> insertData(batchNumber));
        }

        // 关闭线程池
        executor.shutdown();
    }

    private static void insertData(int batchNumber) {
        long startTime = System.currentTimeMillis();
        long startSqlTime = startTime;
        try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT_QUERY)) {
            // 批量插入数据
            for (int i = 0; i < BATCH_SIZE; i++) {
                pstmt.setString(1, generateName());
                pstmt.setInt(2, generateAge());
                pstmt.setString(3, generateEmail());
                pstmt.setString(4, generateAddress());
                pstmt.setString(5, generatePhoneNumber());
                pstmt.setString(6, generateJob());
                pstmt.setString(7, generateCompany());
                pstmt.setDouble(8, generateSalary());
                pstmt.setBoolean(9, generateIsEmployed());
                pstmt.setDate(10, generateBirthDate());
                pstmt.setTimestamp(11, generateRegistrationDate());
                pstmt.setFloat(12, generateHeight());
                pstmt.setFloat(13, generateWeight());
                pstmt.setString(14, generateWebsiteUrl());
                pstmt.setTimestamp(15, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.addBatch();
            }
            startSqlTime = System.currentTimeMillis();
            pstmt.executeBatch();
        } catch (SQLException e) {
            log.error("保存数据时发生错误：{}", e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        log.info("批次：{}，保存数据总耗时：{} ms, 写入数据库耗时: {} ms", batchNumber, endTime - startTime, endTime - startSqlTime);
    }

    // 使用JavaFaker生成假数据的方法
    private static String generateName() {
        return faker.name().fullName();
    }

    private static int generateAge() {
        return faker.number().numberBetween(18, 80);
    }

    private static String generateEmail() {
        return faker.internet().emailAddress();
    }

    private static String generateAddress() {
        return faker.address().fullAddress();
    }

    private static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    private static String generateJob() {
        return faker.job().title();
    }

    private static String generateCompany() {
        return faker.company().name();
    }

    private static double generateSalary() {
        return faker.number().randomDouble(2, 20000, 150000);
    }

    private static boolean generateIsEmployed() {
        return faker.bool().bool();
    }

    private static java.sql.Date generateBirthDate() {
        return new java.sql.Date(faker.date().birthday().getTime());
    }

    private static java.sql.Timestamp generateRegistrationDate() {
        return new java.sql.Timestamp(faker.date().past(10, java.util.concurrent.TimeUnit.DAYS).getTime());
    }

    private static float generateHeight() {
        return (float) faker.number().randomDouble(2, 150, 200);
    }

    private static float generateWeight() {
        return (float) faker.number().randomDouble(2, 50, 150);
    }

    private static String generateWebsiteUrl() {
        return faker.internet().url();
    }

    // JavaFaker实例
    private static final Faker faker = new Faker(Locale.CHINA);
}
