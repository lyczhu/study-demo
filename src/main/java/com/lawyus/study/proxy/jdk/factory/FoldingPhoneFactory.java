package com.lawyus.study.proxy.jdk.factory;

import com.lawyus.study.proxy.jdk.FoldingPhone;
import com.lawyus.study.proxy.jdk.Phone;
import com.lawyus.study.proxy.jdk.PhoneProxy;

import java.lang.reflect.Proxy;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/20 16:44
 */
public class FoldingPhoneFactory implements ProxyFactory {
	@Override
	public Object createProxy() {
		Phone phone = new FoldingPhone();
		PhoneProxy proxy = new PhoneProxy(phone);
		return Proxy.newProxyInstance(phone.getClass().getClassLoader(), phone.getClass().getInterfaces(), proxy);
	}
}
