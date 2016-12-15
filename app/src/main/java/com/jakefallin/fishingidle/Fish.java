package com.jakefallin.fishingidle;

import android.util.Log;

import com.jakefallin.fishingidle.upgrades.Upgrade;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by JakeFallin on 12/8/2016.
 */

public class Fish {

    private double sizeVal;
    private double rarityVal;
    private double value;
    private String species;
    private String rarity;
    private String size;
    private String[] types = {"Carp", "Catfish", "Bass", "Perch", "Salmon", "Trout"};
    private String[] rarities = {"Abundant", "Common", "Uncommon", "Rare", "Exotic", "Fable", "Myth"};
    private String[] sizes = {"Mini", "Small", "Average", "Large", "Giant"};
    ArrayList<Upgrade> upgrades;


    public Fish(TinyDB tinyDB) {

        Random r = new Random();
        upgrades = tinyDB.getListObject("Rod", Upgrade.class);
        rarity = generateRarity(r.nextInt(1000));
        size = generateSize(r.nextInt(1000));
        sizeVal = generateSizeModifier(size);
        Log.e("SIZE B4", "" + sizeVal);
        sizeVal = upgradeModifiers(sizeVal);
        Log.e("SIZE AFTER", "" + sizeVal);
        rarityVal = generateRarityModifier(rarity);
        double initialVal = ((double) r.nextInt(201-100)+100) / 100.0;
        value = initialVal * sizeVal * rarityVal;
        species = types[r.nextInt(6)];

    }

    public String generateRarity(int value) {

        if(value < 400)
            return "Abundant";
        else if(value < 670)
            return "Common";
        else if(value < 820)
            return "Uncommon";
        else if(value < 920)
            return "Rare";
        else if(value < 970)
            return "Exotic";
        else if(value < 995)
            return "Fable";
        else if(value < 1000)
            return "Epic";

        return "Unknown";
    }

    public String generateSize(int value) {

        if(value < 50)
            return "Mini";
        else if(value < 250)
            return "Small";
        else if(value < 750)
            return "Average";
        else if(value < 950)
            return "Large";
        else if(value < 1000)
            return "Giant";

        return "Unknown";
    }

    public double generateSizeModifier(String size) {

        switch (size) {
            case("Mini"):
                return .5;
            case("Small"):
                return .67;
            case("Average"):
                return 1;
            case("Large"):
                return 1.5;
            case("Giant"):
                return 3;
        }
        return 1;
    }

    public double generateRarityModifier(String rarity) {

        switch (rarity) {
            case("Abundant"):
                return .95;
            case("Common"):
                return 1;
            case("Uncommon"):
                return 1.1;
            case("Rare"):
                return 1.5;
            case("Exotic"):
                return 2.5;
            case("Fable"):
                return 10;
            case("Myth"):
                return 25;
        }
        return 1;
    }

    public double upgradeModifiers(double sizeVal) {

        double temp = 0;

        for(int i = 0; i < upgrades.size(); i++) {

            int level = upgrades.get(i).getLevel();
            temp += (double) level * 1.01;

        }
        return sizeVal + (temp / 100);
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getValue() {

        return Math.round(value * 100.0) / 100.0;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getRarityVal() {
        return rarityVal;
    }

    public void setRarityVal(double rarityVal) {
        this.rarityVal = rarityVal;
    }

    public double getSizeVal() {
        return sizeVal;
    }

    public void setSizeVal(double sizeVal) {
        this.sizeVal = sizeVal;
    }
}
