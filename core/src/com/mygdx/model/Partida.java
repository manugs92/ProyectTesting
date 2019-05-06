package com.mygdx.model;

import java.util.ArrayList;

public class Partida {
    //NORMAS DE PARTIDA.
    private final int MAX_CARDS_IN_HAND = 6;

    private ArrayList<Jugador> jugadores = new ArrayList<>();
    private  Carta selectedCard = new Carta();
    private ArrayList<Criatura> criaturasInvocadas;
    private ArrayList<Carta> cartasColocadas;

    private int cantidadCartas;
    public int init=0;

    private ArrayList<Mazo> mazos = new ArrayList<>();
    private Mano manoPartida;




    public Partida(Jugador jugador) {
        this.jugadores.add(jugador);
        this.setSelectedCard(null);
        this.criaturasInvocadas =  new ArrayList<>();
        this.cartasColocadas = new ArrayList<>();
        this.mazos.add(jugador.getMazo());
        this.manoPartida = new Mano(mazos.get(0));
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


    public int getCantidadCartas() { return cantidadCartas; }

    public void setCantidadCartas(int cantidadCartas) {
        this.cantidadCartas = cantidadCartas;
    }

    public Mano getManoPartida() {
        return manoPartida;
    }

    public void setManoPartida(Mano manoPartida) {
        this.manoPartida = manoPartida;
    }

    public ArrayList<Mazo> getMazos() { return mazos; }

    public void addJugador(Jugador jugador) {
        this.jugadores.add(jugador);
        this.mazos.add(jugador.getMazo());
    }
}
