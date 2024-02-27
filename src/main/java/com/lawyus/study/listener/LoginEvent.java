package com.lawyus.study.listener;

import org.springframework.context.ApplicationEvent;

/**
 * Spring 自定义事件监听
 * 登录事件类
 *
 * @author: lyc
 * @date: 2023/12/19
 */
public class LoginEvent extends ApplicationEvent {
    private Login login;

    public LoginEvent(Object source, Login login) {
        super(source);
        this.login = login;
    }

    public Login getLogin() {
        return this.login;
    }
}
