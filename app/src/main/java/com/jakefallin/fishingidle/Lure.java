package com.jakefallin.fishingidle;

/**
 * Created by JakeFallin on 12/15/2016.
 */

public class Lure {

    private String name;
    private double value;
    private double bonus;

    public Lure(String name, double value, double bonus) {

        this.name = name;
        this.value = value;
        this.bonus = bonus;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
}
