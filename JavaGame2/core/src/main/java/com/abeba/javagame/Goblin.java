
package com.abeba.javagame;

public class Goblin extends Charakter {
    public Goblin(int pLp, int pLvl, String pName){
        super(pLp, pLvl, pName);
    }

    @Override
    public int Angriff(Charakter ziel){
        int schadenG = lvl * 5;
        ziel.lp = Math.max(0, ziel.lp - schadenG);
        return schadenG;
    }

    public void heilen(int heilung){
        lp = lp + heilung;
    }
}
