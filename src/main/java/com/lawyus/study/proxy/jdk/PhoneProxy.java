package com.lawyus.study.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/20 11:14
 */
public class PhoneProxy implements InvocationHandler {

	private Phone target;

	public PhoneProxy(Phone target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// 前置增强
		System.out.println("要先输入联系人电话");

		Object result = method.invoke(target, args);

		// 后置增强
		System.out.println("通话完毕后要挂掉电话");
		return result;
	}

	public Phone createProxy() {
		return (Phone) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}

	public <T> T createProxy2() {
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
}
