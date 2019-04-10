package com.mygdx.model;

import java.util.ArrayList;

public class Partida {

    private  Carta selectedCard = new Carta();
    private static ArrayList<Criatura> criaturasInvocadas = new ArrayList<>();
    private ArrayList<Carta> manoJ1 = new ArrayList<Carta>();
    private ArrayList<Carta> manoJ2 = new ArrayList<Carta>();

    public Carta getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Carta carta){
        selectedCard=carta;
    }

    public void addNewInvoquedMonster(Criatura criatura) {
        criaturasInvocadas.add(criatura);
    }

    public  ArrayList<Criatura> getCriaturasInvocadas() {
        return criaturasInvocadas;
    }

    public ArrayList<Carta> getManoJ1() {
        return manoJ1;
    }

    public void setManoJ1(ArrayList<Carta> manoJ1) {
        this.manoJ1 = manoJ1;
    }

    public ArrayList<Carta> getManoJ2() {
        return manoJ2;
    }

    public void setManoJ2(ArrayList<Carta> manoJ2) {
        this.manoJ2 = manoJ2;
    }

}
