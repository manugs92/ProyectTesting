package com.mygdx.model;

import java.util.ArrayList;

public class Partida {
    //NORMAS DE PARTIDA.
    private final int MAX_CARDS_IN_HAND = 6;


    private  Carta selectedCard = new Carta();
    private ArrayList<Criatura> criaturasInvocadas;
    private ArrayList<Carta> cartasColocadas;
    private ArrayList<Carta> manoJ1;
    private int cantidadCartas;
    public int init=0;
    private ArrayList<Carta> manoJ2;
    private ArrayList<Carta> mazoJ1;
    private Mazo mazoSrcJ1 = new Mazo();


    public Partida() {
        this.setSelectedCard(null);
        this.criaturasInvocadas =  new ArrayList<>();
        this.cartasColocadas = new ArrayList<>();
        this.manoJ1 = new ArrayList<Carta>();
        this.manoJ2 = new ArrayList<Carta>();
        this.mazoJ1 = mazoSrcJ1.DefaultDeck();
        for(int i=0;i<5;i++) {
            this.manoJ1.add(mazoJ1.get(i));
        }
        this.cantidadCartas=manoJ1.size();
    }

    public Carta getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Carta carta){
        selectedCard=carta;
    }

    public void addNewInvoquedCard(Carta carta) {
        cartasColocadas.add(carta);
    }

    public  ArrayList<Carta> getInvoquedCards() {
        return cartasColocadas;
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

    public int getCantidadCartas() {
        return cantidadCartas;
    }

    public void setCantidadCartas(int cantidadCartas) {
        this.cantidadCartas = cantidadCartas;
    }

    public ArrayList<Carta> getManoJ2() {
        return manoJ2;
    }

    public void setManoJ2(ArrayList<Carta> manoJ2) {
        this.manoJ2 = manoJ2;
    }

}
