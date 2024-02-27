package com.lawyus.study.listener;

import org.springframework.context.ApplicationListener;

/**
 * Spring 自定义事件监听 的事件类
 * 登录监听器
 *
 * @author: lyc
 * @date: 2023/12/19
 */
public class LoginListener implements ApplicationListener<LoginEvent> {
    @Override
    public void onApplicationEvent(LoginEvent event) {

        System.out.printf("监听到事件：{}", event.getLogin());
    }
}
