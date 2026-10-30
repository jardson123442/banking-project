package com.v1.bms.project.domain.vo.account.opening;

public class Hawk extends Bird{

    @Override
    public void flying() {
        System.out.println("Falcao voando");
    }

    @Override
    public void eat() {
        System.out.println("Falcao comendo");
    }
}
