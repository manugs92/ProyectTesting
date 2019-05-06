package com.mygdx.model;

import java.util.ArrayList;

public class Partida {
    //NORMAS DE PARTIDA.
    private final int MAX_CARDS_IN_HAND = 6;


    private  Carta selectedCard = new Carta();
    private ArrayList<Criatura> criaturasInvocadas;
    private ArrayList<Carta> cartasColocadas;

    private int cantidadCartas;
    public int init=0;


   private  Mazo mazo = new Mazo();
    private Mano manoPartida;




    public Partida() {
        this.setSelectedCard(null);
        this.criaturasInvocadas =  new ArrayList<>();
        this.cartasColocadas = new ArrayList<>();

        this.mazo.DefaultDeck();
        this.manoPartida = new Mano(mazo);
        this.cantidadCartas= manoPartida.getMano().size();
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


    public int getCantidadCartas() {
        return cantidadCartas;
    }

    public void setCantidadCartas(int cantidadCartas) {
        this.cantidadCartas = cantidadCartas;
    }

    public Mano getManoPartida() {
        return manoPartida;
    }

    public void setManoPartida(Mano manoPartida) {
        this.manoPartida = manoPartida;
    }
}
