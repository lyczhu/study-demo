package com.lawyus.study.data;

import com.github.javafaker.Faker;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.concurrent.*;

@Slf4j
public class GenerateDataThreadSafe {

    // JDBC连接池参数
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

    public static void main(String[] args) {

        // 创建HikariCP连接池配置
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(USER);
        config.setPassword(PASS);
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
            Future<?> future = executor.submit(() -> {
                GenerateDataThreadSafe data = new GenerateDataThreadSafe();
                data.insertData(batchNumber);
            });

            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {

                log.error("线程异常信息: {}", e.getMessage());
            }
        }

        // 关闭线程池
        executor.shutdown();
    }

    private void insertData(int batchNumber) {
        long startTime = System.currentTimeMillis();

        // Create a new Faker instance for each thread
        Faker faker = new Faker(Locale.CHINA);

        try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(INSERT_QUERY)) {
            // Batch insert data
            for (int i = 0; i < BATCH_SIZE; i++) {
                pstmt.setString(1, faker.name().fullName());
                pstmt.setInt(2, faker.number().numberBetween(18, 80));
                pstmt.setString(3, faker.internet().emailAddress());
                pstmt.setString(4, faker.address().fullAddress());
                pstmt.setString(5, faker.phoneNumber().phoneNumber());
                pstmt.setString(6, faker.job().title());
                pstmt.setString(7, faker.company().name());
                pstmt.setDouble(8, faker.number().randomDouble(2, 20000, 150000));
                pstmt.setBoolean(9, faker.bool().bool());
                pstmt.setDate(10, new Date(faker.date().birthday().getTime()));
                Timestamp timestamp = new Timestamp(faker.date().past(10, TimeUnit.DAYS).getTime());
                pstmt.setTimestamp(11, timestamp);
                pstmt.setFloat(12, (float) faker.number().randomDouble(2, 150, 200));
                pstmt.setFloat(13, (float) faker.number().randomDouble(2, 50, 150));
                pstmt.setString(14, faker.internet().url());
                pstmt.setTimestamp(15, Timestamp.valueOf(LocalDateTime.now()));
                //log.info("pstmt: {}", pstmt);
                pstmt.addBatch();
            }
            //long generateDataTime = System.currentTimeMillis();
            //log.info("数据生成完成，批次：{}，保存数据耗时：{} ms", batchNumber, generateDataTime - startTime);
            pstmt.executeBatch();
        } catch (SQLException e) {
            log.error("保存数据时发生错误：{}", e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        log.info("批次：{}，保存数据耗时：{} ms", batchNumber, endTime - startTime);
    }
}
