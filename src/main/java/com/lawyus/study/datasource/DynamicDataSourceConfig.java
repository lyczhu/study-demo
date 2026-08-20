package com.lawyus.study.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DynamicDataSourceConfig {

    // 配置主库数据源
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 配置从库数据源
    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    // 配置动态数据源（设为默认数据源）
    @Bean(name = "dynamicDataSource")
    @Primary // 优先使用该数据源
    public DataSource dynamicDataSource(
            @Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("slaveDataSource") DataSource slaveDataSource) {

        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();

        // 1. 设置默认数据源（当未指定标识时使用）
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);

        // 2. 配置所有可切换的数据源（key对应数据源标识，value对应数据源实例）
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master", masterDataSource);
        dataSourceMap.put("slave", slaveDataSource);
        dynamicDataSource.setTargetDataSources(dataSourceMap);

        return dynamicDataSource;
    }
}
