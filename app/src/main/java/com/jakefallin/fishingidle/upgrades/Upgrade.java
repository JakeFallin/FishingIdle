package com.jakefallin.fishingidle.upgrades;

/**
 * Created by jakefallin on 12/9/16.
 */

public class Upgrade {

    private double cost;
    private String name;
    private boolean purchased;
    private Category category;


    public Upgrade(String name, double cost, Category category, boolean purchased) {

        this.name = name;
        this.cost = cost;
        this.purchased = purchased;
        this.category = category;


    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public enum Category {
        reel, line, shaft;
    }

}
