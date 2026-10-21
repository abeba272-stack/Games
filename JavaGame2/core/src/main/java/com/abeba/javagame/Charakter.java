package com.abeba.javagame;
public abstract class Charakter {
    int lp;
    int lvl;
    String name;

    public Charakter(int pLp,int pLvl,String pName){
        this.lp = pLp;
        this.lvl = pLvl;
        this.name = pName;
    }

    public abstract int Angriff(Charakter ziel);
}