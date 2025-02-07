package com.lawyus.study.aspectj.service;

import org.springframework.stereotype.Service;

/**
 * 服务接口实现类
 * 切面接入点
 *
 * @author: lyc
 * @date: 2024/3/12
 */
@Service
public class LoggingServiceImpl implements LoggingService {

    @Override
    public void log() {
        System.out.println("log record");
    }
}
