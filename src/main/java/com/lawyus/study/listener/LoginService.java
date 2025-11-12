package com.lawyus.study.listener;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lang.NonNullApi;

/**
 * Spring 自定义事件监听 的事件类
 * 事件发布业务类
 *
 * @author: lyc
 * @date: 2023/12/19
 */
public class LoginService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(Login login) {
        this.applicationEventPublisher.publishEvent(new LoginEvent("create login event", login));
    }
}
