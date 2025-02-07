package com.lawyus.study.rmi.example;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class MyRemoteClient {
    public static void main(String[] args) {
        float f = 0b10111111000110011001100110011001;
        float f1 = 0b00111111100000000000000000000000;
        System.out.println(f);
        System.out.println(Float.valueOf(f1));
        //new MyRemoteClient().get();
    }

    public void get() {
        try {
            MyRemote service = (MyRemote) Naming.lookup("rmi://127.0.0.1/RemoteHello");
            String message = service.sayHello();
            System.out.println(message);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
