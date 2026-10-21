package com.abeba.javagame;

public class Held extends Charakter {
    public Held(int pLvl, int pLp, String pName) {
        super(pLp, pLvl, pName);
    }
    public void Rennen(int länge){
        System.out.println(name + " ist " + länge + " gelaufen");
    }

    public void schadenBekommen(int schadenB){
        lp = Math.max(0, lp - schadenB);
    }

    @Override
    public int Angriff(Charakter ziel){
        int schadenG;
        if (Math.random() > 0.2) {
            schadenG = lvl * 20;
        } else{
            schadenG = lvl * 10;
        }
        ziel.lp = Math.max(0, ziel.lp - schadenG);
        return schadenG;
    }

    public void statusAnzeigen(){
        System.out.println("Du hast " + lp + " Lebenspunkte, bist auf Level " + lvl + ", und dein name ist " + name);
    }

    public void heilen(int heilung){
        lp = lp + heilung;
    }
    public void levelAufstieg(int anzahl){
        lvl = lvl + anzahl;
    }
}
