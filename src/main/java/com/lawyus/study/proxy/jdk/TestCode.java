package com.lawyus.study.proxy.jdk;

import com.lawyus.study.proxy.jdk.factory.FoldingPhoneFactory;
import com.lawyus.study.proxy.jdk.factory.ProxyFactory;
import com.lawyus.study.proxy.jdk.factory.SimpleProxyFactory;
import com.lawyus.study.proxy.jdk.factory.SmartPhoneFactory;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/20 16:52
 */
public class TestCode {
	public static void main(String[] args) {
		foldingPhoneProxy();
	}

	public static void smart() {
		ProxyFactory factory = new SmartPhoneFactory();
		Object obj = factory.createProxy();
		Phone phone = (Phone) obj;
		phone.call();
	}

	public static void fold() {
		ProxyFactory factory = new FoldingPhoneFactory();
		Object obj = factory.createProxy();
		Phone phone = (Phone) obj;
		phone.call();
	}

	public static void simpleFold() {
		Object obj = SimpleProxyFactory.createProxy("fold");
		Phone phone = (Phone) obj;
		phone.call();
	}

	public static void phoneProxy() {
		Phone phone = new SmartPhone();
		PhoneProxy proxy = new PhoneProxy(phone);
		Phone obj = proxy.createProxy();
		obj.call();
	}

	public static void foldingPhoneProxy() {
		Phone phone = new FoldingPhone();
		PhoneProxy proxy = new PhoneProxy(phone);
		Phone obj = proxy.createProxy2();
		obj.call();
	}
}
