package com.lawyus.study.datasource;

public class DynamicDataSourceContextHolder {
    // 用ThreadLocal存储当前线程的数据源标识
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源标识
     * @param dataSourceKey 数据源名称（如 "master"、"slave"）
     */
    public static void setDataSourceKey(String dataSourceKey) {
        CONTEXT_HOLDER.set(dataSourceKey);
    }

    /**
     * 获取当前线程的数据源标识
     * @return 数据源名称
     */
    public static String getDataSourceKey() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除当前线程的数据源标识（防止内存泄漏）
     */
    public static void clearDataSourceKey() {
        CONTEXT_HOLDER.remove();
    }
}
