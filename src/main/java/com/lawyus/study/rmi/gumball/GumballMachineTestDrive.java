package com.lawyus.study.rmi.gumball;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class GumballMachineTestDrive {
    public static void main(String[] args) {
        GumballMachineRemote gumballMachineRemote = null;
        int count;
        if (args.length < 2) {
            System.out.println("GumballMachine <name> <inventory>");
            System.exit(1);
        }

        count = Integer.parseInt(args[1]);
        try {
            gumballMachineRemote = new GumballMachine(args[0], count);
            Naming.rebind("//" + args[0] + "gumballmachine", gumballMachineRemote);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
