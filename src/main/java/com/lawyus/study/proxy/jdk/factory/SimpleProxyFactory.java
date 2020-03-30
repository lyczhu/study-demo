package com.lawyus.study.proxy.jdk.factory;

import com.lawyus.study.proxy.jdk.FoldingPhone;
import com.lawyus.study.proxy.jdk.Phone;
import com.lawyus.study.proxy.jdk.PhoneProxy;
import com.lawyus.study.proxy.jdk.SmartPhone;

import java.lang.reflect.Proxy;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/20 16:25
 */
public class SimpleProxyFactory {

	public static Object createProxy(String name) {
		Phone phone = null;
		switch (name) {
			case "fold" :
				phone = new FoldingPhone();
				break;
			default: phone = new SmartPhone();
		}
		PhoneProxy proxy = new PhoneProxy(phone);
		return Proxy.newProxyInstance(phone.getClass().getClassLoader(), phone.getClass().getInterfaces(), proxy);
	}
}
