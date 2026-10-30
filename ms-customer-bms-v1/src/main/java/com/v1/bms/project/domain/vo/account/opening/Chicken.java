package com.v1.bms.project.domain.vo.account.opening;

public class Chicken extends Bird {

    @Override
    public void flying() {
        System.out.println("Galinha voando");
    }

    @Override
    public void eat() {
        System.out.println("Galinha comendo");
    }
}
