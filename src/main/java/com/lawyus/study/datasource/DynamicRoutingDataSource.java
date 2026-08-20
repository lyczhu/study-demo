package com.lawyus.study.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    /**
     * 核心方法：返回当前要使用的数据源标识
     * @return 数据源key（与配置的数据源map中的key对应）
     */
    @Override
    protected Object determineCurrentLookupKey() {
        // 从ThreadLocal中获取当前线程指定的数据源标识
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }
}
