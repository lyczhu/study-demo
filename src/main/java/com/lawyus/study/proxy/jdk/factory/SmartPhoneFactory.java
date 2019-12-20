package com.lawyus.study.proxy.jdk.factory;

import com.lawyus.study.proxy.jdk.Phone;
import com.lawyus.study.proxy.jdk.PhoneProxy;
import com.lawyus.study.proxy.jdk.SmartPhone;

import java.lang.reflect.Proxy;

/**
 * TODO : Describe please!
 *
 * @author: lyc
 * @date: 2019/12/20 16:41
 * @since: 1.0.0
 */
public class SmartPhoneFactory implements ProxyFactory {

	@Override
	public Object createProxy() {
		Phone phone = new SmartPhone();
		PhoneProxy proxy = new PhoneProxy(phone);
		return Proxy.newProxyInstance(phone.getClass().getClassLoader(), phone.getClass().getInterfaces(), proxy);
	}
}
