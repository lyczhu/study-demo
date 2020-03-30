package com.lawyus.study.thread;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2020/3/12 15:17
 */
public class Visibility {

	int i = 1;
	volatile boolean f = false;

	public void write() {
		i = 100;
		//f = true;
	}

	public void read() {
		System.out.println(i);
	}

	public static void main(String[] args) {
		Visibility v = new Visibility();
		v.write();

		Runnable r1 = () -> v.write();
		new Thread(r1).start();

		Runnable r2 = () -> v.read();
		new Thread(r2).start();
	}
}
