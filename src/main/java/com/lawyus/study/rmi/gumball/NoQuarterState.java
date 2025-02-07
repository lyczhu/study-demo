package com.lawyus.study.rmi.gumball;

public class NoQuarterState implements State {
    transient GumballMachine gumballMachine;

    @Override
    public void insertQuarter() {

    }

    @Override
    public void rejectQuarter() {

    }

    @Override
    public void turnCrank() {

    }

    @Override
    public void dispense() {

    }
}
